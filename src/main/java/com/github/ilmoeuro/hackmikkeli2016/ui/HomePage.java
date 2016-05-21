/*
 * Copyright (C) 2016 Ilmo Euro <ilmo.euro@gmail.com>
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

import com.github.ilmoeuro.hackmikkeli2016.UserVoteView;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Ilmo Euro <ilmo.euro@gmail.com>
 */
public class HomePage extends Page {

    private final IModel<UserVoteView> model;

    public HomePage() {
        model = new HmModel<>(
                new UserVoteView(
                        HmApplication.get().getSessionRunner()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        HmForm<UserVoteView> form = new HmForm<>("voteForm",  model);
        form.add(
            new HmListView<UserVoteView.VotesRow>(
                "votes",
                model,
                row -> {
                    row.add(
                        new HmListView<UserVoteView.VotesCell>(
                            "cells",
                            cell -> {
                                HmCheckBox box =
                                    new HmCheckBox(
                                        "vote",
                                        cell);
                                cell.add(box);
                                cell.add(new FormComponentLabel("voteLabel", box));
                            }));}));
                            
        add(form);
    }
}
