/**
 * Copyright 2013 Joan Zapata
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fubaisum.baseadapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Allows an abstraction of the ViewHolder pattern.<br>
 */
public class BaseAdapterViewHolder {

    private final SparseArray<View> views;
    private int layoutId;
    private int position;
    private View convertView;

    protected BaseAdapterViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.views = new SparseArray<View>();
        this.layoutId = layoutId;
        this.position = position;
        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        convertView.setTag(this);
    }

    /**
     * This method is the only entry point to get a BaseAdapterViewHolder.
     *
     * @param context     The current context.
     * @param convertView The convertView arg passed to the getView() method.
     * @param parent      The parent arg passed to the getView() method.
     * @return A BaseAdapterViewHolder instance.
     */
    public static BaseAdapterViewHolder get(
            Context context, View convertView, ViewGroup parent, int layoutId) {
        return get(context, convertView, parent, layoutId, -1);
    }

    /**
     * This method is package private and should only be used by QuickAdapter.
     */
    static BaseAdapterViewHolder get(
            Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (null == convertView) {
            return new BaseAdapterViewHolder(context, parent, layoutId, position);
        }
        // Retrieve the existing helper and update its position
        BaseAdapterViewHolder existingHelper = (BaseAdapterViewHolder) convertView.getTag();
        if (existingHelper.layoutId != layoutId) {
            return new BaseAdapterViewHolder(context, parent, layoutId, position);
        }
        existingHelper.position = position;

        return existingHelper;
    }

    /**
     * This method allows you to retrieve a view and perform custom operations
     * on it, not covered by the BaseAdapterViewHolder.<br/>
     * If you think it's a common use case, please consider creating a new issue
     * at https://github.com/JoanZapata/base-adapter-helper/issues.
     *
     * @param viewId The id of the view you want to retrieve.
     */
    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T retrieveView(int viewId) {

        View view = views.get(viewId);
        if (null == view) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }

        return (T) view;
    }

    /**
     * Retrieve the convertView
     */
    public View getConvertView() {
        return convertView;
    }

    /**
     * Retrieve the overall position of the data in the list.
     *
     * @throws IllegalArgumentException If the position hasn't been set at the construction of the
     *                                  this helper.
     */
    public int getPosition() {
        if (position == -1) {
            throw new IllegalStateException("Use BaseAdapterViewHolder constructor "
                    + "with position if you need to retrieve the position.");
        }
        return position;
    }
}
