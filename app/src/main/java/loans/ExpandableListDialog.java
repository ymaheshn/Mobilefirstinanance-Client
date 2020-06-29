package loans;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;
import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.List;

import kyc.dto.BranchTree;

public class ExpandableListDialog extends Dialog implements View.OnClickListener {

    private final EditText editText;
    private final List<BranchTree> cityList;
    private MultiLevelRecyclerView list;
    private EditText filterText = null;
    //    ArrayAdapter<String> adapter = null;
    private static final String TAG = "CityList";
    private MyAdapter myAdapter;
    //    private final MyListAdapter adapter;

    public ExpandableListDialog(Context context, String titleText, EditText editText, List<BranchTree> cityList) {
        super(context);

        /** Design the dialog in main.xml file */
        setContentView(R.layout.alert_search_list);
        this.editText = editText;

        this.cityList = cityList;
        Window window = getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.85), (int) (size.y * 0.7));
        window.setGravity(Gravity.CENTER);

        TextView title = findViewById(R.id.text_title);
        title.setText(titleText);
        filterText = findViewById(R.id.EditBox);
        filterText.addTextChangedListener(filterTextWatcher);
        list = findViewById(R.id.List);
        if (cityList != null && cityList.size() > 0) {
            setMyAdapter();
        }


    }


    private void setMyAdapter() {
        List<RecyclerViewItem> items = prepareData(cityList, 0);
        myAdapter = new MyAdapter(getContext(), items, list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        list.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(myAdapter);
    }

    private List<RecyclerViewItem> prepareData(List<BranchTree> children, int index) {
        ArrayList<RecyclerViewItem> items = new ArrayList<>();
        for (BranchTree branchTree : children) {

            Item item = new Item(branchTree.treeLevel.split("/").length - 1);
            item.setText(branchTree.name);
            if (branchTree.children != null && branchTree.children.size() > 0) {
                index = index + 1;
                item.addChildren(prepareData(branchTree.children, index));
            }
            items.add(item);
        }
        return items;
    }

    @Override
    public void onClick(View v) {

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (myAdapter != null) {
                myAdapter.getFilter().filter(s.toString());
            }
        }
    };

    @Override
    public void onStop() {
        filterText.removeTextChangedListener(filterTextWatcher);
    }


    public class Item extends RecyclerViewItem {
        String text = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        Item(int level) {
            super(level);
        }

    }


    public class MyAdapter extends MultiLevelAdapter implements Filterable {
        private MyAdapter.Holder mViewHolder;
        private Context mContext;
        private List<RecyclerViewItem> mOriginalItems = new ArrayList<>();
        private List<RecyclerViewItem> mListItems;
        private List<RecyclerViewItem> filterList = new ArrayList<>();
        private RecyclerViewItem mItem;

        MyAdapter(Context mContext, List<RecyclerViewItem> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView) {
            super(mListItems);
            this.mListItems = mListItems;
            this.mOriginalItems.addAll(mListItems);
            this.mContext = mContext;
        }

        private void setExpandButton(ImageView expandButton, boolean isExpanded) {
            // set the icon based on the current state
            expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_down_black_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mViewHolder = (MyAdapter.Holder) holder;
            mItem = mListItems.get(position);

            switch (getItemViewType(position)) {
                case 0:
                case 1:
                    holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 2:
                    holder.itemView.setBackgroundColor(Color.parseColor("#f4f4f4"));
                    break;
                case 3:
                    holder.itemView.setBackgroundColor(Color.parseColor("#dfdfdf"));
                    break;
                case 4:
                    holder.itemView.setBackgroundColor(Color.parseColor("#dAdada"));
                    break;
            }
            Item item = (Item) mItem;
            mViewHolder.mTitle.setText(item.getText());

            if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
                setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
                mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.mExpandButton.setVisibility(View.GONE);
            }

            // indent child items
            // Note: the parent item should start at zero to have no indentation
            // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
            float density = mContext.getResources().getDisplayMetrics().density;
            ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).leftMargin = (int) ((item.getLevel() * 3) * density + 0.5f);
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private class Holder extends RecyclerView.ViewHolder {

            TextView mTitle;
            ImageView mExpandIcon;
            LinearLayout mTextBox, mExpandButton;

            Holder(View itemView) {
                super(itemView);
                mTitle = itemView.findViewById(R.id.title);
                mExpandIcon = itemView.findViewById(R.id.image_view);
                mTextBox = itemView.findViewById(R.id.text_box);
                mExpandButton = itemView.findViewById(R.id.expand_field);

                // The following code snippets are only necessary if you set multiLevelRecyclerView.removeItemClickListeners(); in MainActivity.java
                // this enables more than one click event on an item (e.g. Click Event on the item itself and click event on the expand button)

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final RecyclerViewItem item = mListItems.get(getAdapterPosition());
//                        if (item.datum != null) {
//                            iOnItemClickListener.onItemClicked(item.datum);
//                        } else {
                                Item currentItem = (Item) item;
                                mExpandIcon.animate().rotation(currentItem.isExpanded() ? 0 : -180).start();

                                if (!currentItem.hasChildren()) {
                                    editText.setText(currentItem.text);
                                    dismiss();
                                }
                            }
                        }, 500);
//                        }
                        //set click event on item here
//                    Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d was clicked!", getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<RecyclerViewItem> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(mOriginalItems);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    filterList.clear();
                    filterList.addAll(searchResults(mOriginalItems, filterPattern));
                    filteredList.addAll(filterList);
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List values = (List) results.values;
                if (constraint == null || constraint.length() == 0) {
                    setMyAdapter();
                } else {
                    mListItems.clear();
                    mListItems.addAll(values);
                    notifyDataSetChanged();
                }
            }


            private ArrayList<RecyclerViewItem> searchResults(List<RecyclerViewItem> viewItems, String filterPattern) {
                ArrayList<RecyclerViewItem> results = new ArrayList<>();
                for (RecyclerViewItem item : viewItems) {
                    if (!item.hasChildren()) {
                        if (((Item) item).text.toLowerCase().contains(filterPattern.toLowerCase())) {
                            results.add(item);
                        }
                    } else {
                        ArrayList<RecyclerViewItem> items = searchResults(item.getChildren(), filterPattern);
                        if (items.size() != 0) {
                            item.getChildren().clear();
                            item.addChildren(items);
                            results.add(item);
                        }
                    }
                }
                return results;
            }
        };


    }
}