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

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class HmPage extends WebPage {
    private static final long serialVersionUID = 0l;

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if (HmSession.get().isTemporary() ||
            !HmSession.get().isSignedIn()) {
            HmApplication.get().restartResponseAtSignInPage();
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        PackageResourceReference cssRef = 
            new PackageResourceReference(HmPage.class, "HmPage.css");
        response.render(CssHeaderItem.forReference(cssRef));

        ResourceReference jquery =
            new WebjarsJavaScriptResourceReference("jquery/1.11.1/jquery.js");
        ResourceReference jqueryUi =
            new WebjarsJavaScriptResourceReference("jquery-ui/1.11.4/jquery-ui.js");
        ResourceReference jqueryUiCss =
            new WebjarsCssResourceReference("jquery-ui/1.11.4/jquery-ui.min.css");
        ResourceReference jqueryUiThemeCss =
            new PackageResourceReference(HmPage.class, "jquery-ui.theme.css");

        response.render(CssHeaderItem.forReference(jqueryUiCss));
        response.render(CssHeaderItem.forReference(jqueryUiThemeCss));
        response.render(JavaScriptHeaderItem.forReference(jquery));
        response.render(JavaScriptHeaderItem.forReference(jqueryUi));

        PackageResourceReference jsRef =
            new PackageResourceReference(HmPage.class, "HmPage.js");
        response.render(JavaScriptHeaderItem.forReference(jsRef));
    }
}
