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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class HmLink extends Link<Serializable> {
    private static final long serialVersionUID = 0l;
    private final SerializableAction action;

    public HmLink(String id, SerializableAction action) {
        super(id, Model.of());
        this.action = action;
    }

    public HmLink(String id, IModel<?> model, SerializableAction action) {
        super(id, new PropertyModel<>(model, id));
        this.action = action;
    }

    @Override
    public void onClick() {
        action.execute();
        addStateChange();
    }
}
