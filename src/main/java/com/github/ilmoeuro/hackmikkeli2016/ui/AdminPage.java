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

import com.github.ilmoeuro.hackmikkeli2016.AdminView;
import com.github.ilmoeuro.hackmikkeli2016.Plan;
import com.github.ilmoeuro.hackmikkeli2016.PlanComment;
import com.github.ilmoeuro.hackmikkeli2016.PlanProposal;
import com.github.ilmoeuro.hackmikkeli2016.UserVoteView;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 *
 * @author Ilmo Euro <ilmo.euro@gmail.com>
 */
public class AdminPage extends HmPage {

    private final IModel<AdminView> model;
    private final Image image;
    private final WebMarkupContainer commentContainer;

    public AdminPage() {
        model = new HmModel<>(
                new AdminView(
                        HmApplication.get().getSessionRunner()));
        PackageResourceReference examplePicture =
                new PackageResourceReference(AdminPage.class, "example1.png");
        image = new Image("planImage", examplePicture);
        commentContainer = new WebMarkupContainer("commentContainer");
    }

    @Override
    protected void onConfigure() {
        super.onConfigure(); //To change body of generated methods, choose Tools | Templates.
        image.setVisible(model.getObject().getProposal() != null);
        commentContainer.setVisible(model.getObject().getProposal() != null);

        if (model.getObject().getProposal() != null) {
            PackageResourceReference picture =
                    new PackageResourceReference(
                            AdminPage.class,
                            model.getObject().getProposal().getFilename());

            image.setImageResourceReference(picture);
        }
    }

    

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new HmListView<Plan>(
            "availablePlans",
            model,
            item -> {
                HmLink link =
                    new HmLink(
                            "jumpToPlan",
                            () -> {
                                model.getObject().setPlan(item.getModelObject());
                            });
                link.add(
                    new HmLabel("title", item)
                );
                item.add(link);
            }
        ));

        add(new HmListView<PlanProposal>(
            "availableProposals",
            model,
            item -> {
                HmLink link =
                    new HmLink(
                            "jumpToProposal",
                            () -> {
                                model.getObject().setProposal(item.getModelObject());
                            });
                link.add(
                    new HmLabel("title", item)
                );
                item.add(link);
            }
        ));

        add(image);

        add(commentContainer);
        commentContainer.add(new HmListView<PlanComment>(
                "comments",
                model,
                item -> {
                    item.add(
                        new HmLabel("text", item)
                    ); }));

        add(
            new HmListView<AdminView.VotesRow>(
                "votes",
                model,
                row -> {
                    row.add(
                        new HmListView<AdminView.VotesCell>(
                            "cells",
                            row,
                            cell -> {
                                cell.add(new HmLabel("numVotes", cell));
                                cell.add(
                                        AttributeModifier.replace("style",
                                            new PropertyModel(cell.getModel(), "style")));
                            }));}));
    }
}
