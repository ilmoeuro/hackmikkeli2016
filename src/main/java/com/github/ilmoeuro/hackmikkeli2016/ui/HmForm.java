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

import java.util.Objects;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class HmForm<T> extends Form<T> {
    private static final long serialVersionUID = 0l;
    private T oldModelObject = null;

    public HmForm(String id) {
        super(id);
    }

    public HmForm(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        T modelObject = getModelObject();

        if (!Objects.equals(oldModelObject, modelObject)) {
            clearInput();
            oldModelObject = modelObject;
        }
    }
}
