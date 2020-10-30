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
     * @param packageNames 仅扫描指定包名下的类。可输入多个包名
     * @return
     */
    public LiveData<List<IDClassInfo>> getEntryClassList(final Class preModuleClz, String... packageNames){
        new Thread(new Runnable() {//todo 创建了线程
            @Override
            public void run() {

                List<IDClassInfo> infoList = preModuleClz == null ? IDemoHelper.getModuleClassListSync(packageNames) : IDemoHelper.getModuleClassListSync(preModuleClz, packageNames);

                entryListData.postValue(infoList);
            }
        }).start();

        return entryListData;
    }
}
