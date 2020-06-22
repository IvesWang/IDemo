package cc.ives.aeg.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.ives.aeg.AutoEntryGenerator;
import cc.ives.aeg.annotation.EntryClassInfo;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
public class AutoEntryListVM extends ViewModel {
    private MutableLiveData<List<EntryClassInfo>> entryListData = new MutableLiveData<>();

    public LiveData<List<EntryClassInfo>> getEntryClassList(){
        new Thread(new Runnable() {//todo 创建了线程
            @Override
            public void run() {
                List<EntryClassInfo> infoList = null;
                infoList = AutoEntryGenerator.scan();

                Collections.sort(infoList, new Comparator<EntryClassInfo>() {
                    @Override
                    public int compare(EntryClassInfo o1, EntryClassInfo o2) {
                        return o1.getIndexTime() - o2.getIndexTime();
                    }
                });

                entryListData.postValue(infoList);
            }
        }).start();
        return entryListData;
    }
}
