package cc.ives.idemo.ui;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import cc.ives.idemo.IDemoContext;
import cc.ives.idemo.annotation.IDItemInfo;
import cc.ives.idemo.util.IDLog;
import cc.ives.idemo.util.IDemoHelper;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
@Deprecated
public class AutoEntryOldListFragment extends ListFragment {
    private static final String TAG = AutoEntryOldListFragment.class.getSimpleName();

    private Class preEntryClz;// 本fragment展示的前一个操作入口，首个fragment则为null
    private List<IDItemInfo> entryClassInfoList;

    private UIAction uiAction;
    private String[] packageNames;

    public AutoEntryOldListFragment() {
    }

    public void setPackageNames(String[] packageNames) {
        this.packageNames = packageNames;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IDemoContext.setAppContext(getActivity().getApplicationContext());
        readPreEntryInfo();
        uiAction = new UIAction();
    }

    private void readPreEntryInfo(){
        Bundle argumentBundle = getArguments();
        if (argumentBundle != null){
            preEntryClz = (Class) argumentBundle.get(UIAction.KEY_ARGUMENT_PRE_MODULE_CLZ);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getView()).setBackgroundColor(Color.WHITE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                entryClassInfoList = preEntryClz == null ? IDemoHelper.getModuleClassListSync(packageNames) : IDemoHelper.getModuleClassListSync(preEntryClz, packageNames);

                List<String> classDescList = entryClassInfoList.stream().map(new Function<IDItemInfo, String>() {
                    @Override
                    public String apply(IDItemInfo entryClassInfo) {
                        // 显示的名称
                        return entryClassInfo.getItemName();
                    }
                }).collect(Collectors.<String>toList());// !!!<String>

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, classDescList);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        setListAdapter(arrayAdapter);
                    }
                });
            }
        }).start();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        IDItemInfo entryClassInfo = entryClassInfoList.get(position);
        IDLog.i(TAG, String.format("onListItemClick() 点击了:%s", entryClassInfo.getItemName()));

        uiAction.onItemClick(entryClassInfo, getActivity(), getFragmentManager());
    }
}
