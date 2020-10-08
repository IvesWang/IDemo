package cc.ives.idemo.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import cc.ives.idemo.IDemoContext;
import cc.ives.idemo.annotation.IDClassInfo;
import cc.ives.idemo.util.IDLog;

/**
 * @author wangziguang
 * @date 2020/5/23 0023
 * @description
 */
public class AutoEntryListFragment extends ListFragment {
    private static final String TAG = AutoEntryListFragment.class.getSimpleName();

    private Class preEntryClz;// 本fragment展示的前一个操作入口，首个fragment则为null
    private List<IDClassInfo> entryClassInfoList;

    private UIAction uiAction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IDemoContext.setAppContext(getContext().getApplicationContext());
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

        ViewModelProviders.of(this)
                .get(AutoEntryListVM.class)
                .getEntryClassList(preEntryClz)// 空则表示是根节点页面
                .observe(this, new Observer<List<IDClassInfo>>() {

                    @Override
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onChanged(List<IDClassInfo> entryClassInfos) {
                        entryClassInfoList = entryClassInfos;
                        // todo 到这里getContext有没有可能是空的？会不会执行到这的时候fragemnt才被销毁？

                        List<String> classDescList = entryClassInfos.stream().map(new Function<IDClassInfo, String>() {
                            @Override
                            public String apply(IDClassInfo entryClassInfo) {
                                // 优先返回desc，否则返回类名
                                if (TextUtils.isEmpty(entryClassInfo.getDesc())){
                                    return entryClassInfo.getCurrentClz().getSimpleName();
                                }
                                return entryClassInfo.getDesc();
                            }
                        }).collect(Collectors.<String>toList());// !!!<String>

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, classDescList);
                        setListAdapter(arrayAdapter);
                    }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        IDClassInfo entryClassInfo = entryClassInfoList.get(position);
        IDLog.i(TAG, String.format("onListItemClick() 点击了:%s", entryClassInfo.getCurrentClz().getCanonicalName()));

        uiAction.onItemClick(entryClassInfo, getActivity(), getFragmentManager());
    }

    @Override
    public void onDestroyView() {
        IDLog.i(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        IDLog.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        IDLog.i(TAG, "onDetach()");
        super.onDetach();
    }
}
