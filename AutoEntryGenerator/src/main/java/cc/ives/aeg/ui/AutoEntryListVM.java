package cc.ives.aeg.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import cc.ives.aeg.annotation.EntryClassInfo;
import cc.ives.aeg.util.AegHelper;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
public class AutoEntryListVM extends ViewModel {
    private MutableLiveData<List<EntryClassInfo>> entryListData = new MutableLiveData<>();

    /**
     * 返回扫描到的类信息
     * 调用前必须要保证Context已经初始化
     * @return
     */
    public LiveData<List<EntryClassInfo>> getEntryClassList(){
        new Thread(new Runnable() {//todo 创建了线程
            @Override
            public void run() {

                List<EntryClassInfo> infoList = AegHelper.getEntryClassListSync();

                entryListData.postValue(infoList);
            }
        }).start();

        return entryListData;
    }
}
