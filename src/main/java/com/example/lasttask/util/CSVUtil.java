package com.example.lasttask.util;

import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.collection.FieldEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.model.entity.item.ItemFieldEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CSVUtil {

    public static List<String[]> collectionItemFieldNameLists(List<FieldEntity> fieldList) {
        int length = fieldList.size();
        String[] head = new String[length + 3];
        head[0] = "Id";
        head[1] = "Name";
        head[2] = "Tags";
        for (int i = 0; i < length; i++) {
            head[i + 3] = fieldList.get(i).getName();
        }
        return Collections.singletonList(head);
    }

    public static List<String[]> valuesCollectionItemFields(ItemEntity item, List<ItemFieldEntity> itemFieldList, int fieldCount) {
        final List<String[]> fieldData = new ArrayList<>();
        String[] itemContents = new String[fieldCount + 3];
        int columnCount = 0, i = 0;
        while (i < itemFieldList.size()) {
            itemContents[columnCount++] = String.valueOf(item.getId());
            itemContents[columnCount++] = item.getName();
            StringBuilder st = new StringBuilder();
            item.getTagList().forEach(tagEntity -> {
                st.append(tagEntity.getName() + ", ");
            });
            st.deleteCharAt(st.length() - 2);
            itemContents[columnCount++] = st.toString();
            for (int j = 0; j < fieldCount; j++) {
                itemContents[columnCount++] = itemFieldList.get(i++).getData();
            }
            fieldData.add(itemContents);
            columnCount = 0;
        }
        return fieldData;
    }
}