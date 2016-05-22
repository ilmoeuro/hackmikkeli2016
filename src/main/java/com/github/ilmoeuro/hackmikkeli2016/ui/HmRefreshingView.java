/*
 * Copyright (C) 2015 Ilmo Euro <ilmo.euro@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.ilmoeuro.hackmikkeli2016.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class HmRefreshingView<T> extends RefreshingView<T>{
    private static final long serialVersionUID = 0l;

    @FunctionalInterface
    public interface Populator<T> extends Serializable {
        void populateItem(Item<T> item);
    }

    @FunctionalInterface
    public interface ItemSupplier<T> extends Serializable {
        Iterator<IModel<T>> supplyItems();
    }

    private final Populator<T> populate;
    private final ItemSupplier<T> supply;

    public HmRefreshingView(
        String id,
        Populator<T> populate,
        ItemSupplier<T> supply
    ) {
        super(id);
        this.populate = populate;
        this.supply = supply;
    }

    public HmRefreshingView(
        String id,
        ListItem<?> item,
        Populator<T> populate
    ) {
        this(id, item.getModel(), populate);
    }

    public HmRefreshingView(
        String id,
        IModel<?> model,
        Populator<T> populate
    ) {
        super(id);
        IModel<Iterable<T>> iterableModel = new PropertyModel<>(model, id);
        this.populate = populate;
        this.supply = () -> {
            List<IModel<T>> models = new ArrayList<>();
            int i = 0;
            for (T val : iterableModel.getObject()) {
                models.add(new PropertyModel<>(
                        iterableModel,
                        String.valueOf(i)));
                i++;
            }
            return models.iterator();
        };
    }

    @Override
    protected void populateItem(Item<T> item) {
        populate.populateItem(item);
    }

    @Override
    protected Iterator<IModel<T>> getItemModels() {
        return supply.supplyItems();
    }
}
