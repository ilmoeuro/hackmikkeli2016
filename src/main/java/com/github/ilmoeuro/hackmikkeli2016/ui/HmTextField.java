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

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class HmTextField<T> extends TextField<T> {
    private static final long serialVersionUID = 0l;

    public HmTextField(String id) {
        super(id);
    }

    public HmTextField(String id, IModel<?> baseModel) {
        super(id, new PropertyModel<>(baseModel, id));
    }

    public HmTextField(String id, ListItem<?> item) {
        super(id, new PropertyModel<>(item.getModel(), id));
    }
}
