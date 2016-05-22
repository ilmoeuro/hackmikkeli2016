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

import com.github.ilmoeuro.hackmikkeli2016.ConfigProvider;
import com.github.ilmoeuro.hackmikkeli2016.DataSourceInitializer;
import com.github.ilmoeuro.hackmikkeli2016.DatabaseInitializer;
import com.github.ilmoeuro.hackmikkeli2016.DefaultExampleData;
import com.github.ilmoeuro.hackmikkeli2016.SessionRunner;
import com.github.ilmoeuro.hackmikkeli2016.TypesafeConfigProvider;
import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;
import java.time.LocalDate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.pages.SignInPage;
import org.apache.wicket.markup.html.WebPage;
import org.jooq.DSLContext;

@Slf4j
public final class HmApplication extends AuthenticatedWebApplication {

    @Getter
    private final ConfigProvider configProvider;
    @Getter
    private final SessionRunner sessionRunner;
    private final DataSourceInitializer dsInitializer;
    private final DatabaseInitializer dbInitializer;

    public HmApplication() {
        configProvider
            = new TypesafeConfigProvider();
        sessionRunner
            = new SessionRunner(configProvider);
        dsInitializer
            = new DataSourceInitializer(configProvider);
        dbInitializer
            = new DatabaseInitializer(
                    configProvider,
                    new DefaultExampleData());
    }

    public HmApplication(
        ConfigProvider configProvider,
        SessionRunner sessionRunner,
        DataSourceInitializer dsInitializer,
        DatabaseInitializer dbInitializer
    ) {
        this.configProvider = configProvider;
        this.sessionRunner = sessionRunner;
        this.dsInitializer = dsInitializer;
        this.dbInitializer = dbInitializer;
    }

    @Override
    public void init() {
        super.init();
        dsInitializer.init();
        sessionRunner.exec((DSLContext jooq) -> {
            dbInitializer.init(jooq);
        });

        WebjarsSettings webjarsSettings = new WebjarsSettings();
        WicketWebjars.install(this, webjarsSettings);

        mountPage("/admin", AdminPage.class);

        getMarkupSettings().setStripWicketTags(true);
    }

    @SuppressWarnings("unchecked")
    public static HmApplication get() {
        try {
            return (HmApplication) Application.get();
        } catch (ClassCastException ex) {
            String error = "Called get() from wrong app";
            log.error(error, ex);
            throw new RuntimeException(error, ex);
        }
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = new ConverterLocator();
        return locator;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return HmSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }
}
