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

import com.github.ilmoeuro.hackmikkeli2016.Refreshable;
import org.apache.wicket.model.CompoundPropertyModel;

public class
    HmModel<T extends Refreshable>
extends
    CompoundPropertyModel<T>
{
    private static final long serialVersionUID = 0l;

    private boolean dirty;

    public HmModel(T model) {
        super(model);
        this.dirty = true;
    }

    @Override
    public T getObject() {
        T obj = super.getObject();
        if (dirty) {
            obj.refresh();
            dirty = false;
        }
        return super.getObject();
    }

    @Override
    public void setObject(T object) {
        super.setObject(object);
    }

    @Override
    public void detach() {
        super.detach();
        dirty = true;
    }

    
}
