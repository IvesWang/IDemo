package cc.ives.idemo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import cc.ives.idemo.annotation.IDClassInfo;
import cc.ives.idemo.util.IDemoHelper;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
public class AutoEntryListVM extends ViewModel {
    private MutableLiveData<List<IDClassInfo>> entryListData = new MutableLiveData<>();

    /**
     * 返回扫描到的类信息
     * 调用前必须要保证Context已经初始化
     * @return
     */
    public LiveData<List<IDClassInfo>> getEntryClassList(final Class preModuleClz){
        new Thread(new Runnable() {//todo 创建了线程
            @Override
            public void run() {

                List<IDClassInfo> infoList = preModuleClz == null ? IDemoHelper.getModuleClassListSync() : IDemoHelper.getModuleClassListSync(preModuleClz);

                entryListData.postValue(infoList);
            }
        }).start();

        return entryListData;
    }
}
