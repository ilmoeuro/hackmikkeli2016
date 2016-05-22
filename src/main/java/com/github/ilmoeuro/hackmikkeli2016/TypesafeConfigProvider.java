/* 
 * Copyright (C) 2015 Ilmo Euro
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
package com.github.ilmoeuro.hackmikkeli2016;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.net.URL;

public final class TypesafeConfigProvider implements ConfigProvider {

    private static final String CONF_FILE = "hackmikkeli2016.properties";
    private static final String CONF_PROPERTY = "hackmikkeli2016.config";

    private final Config config;

    public TypesafeConfigProvider() {
        final URL url
            = ResourceRoot.class.getResource(CONF_FILE);
        if (url == null) {
            throw new RuntimeException(CONF_FILE + " not found");
        }
        final String configFileLocation
            = System.getProperty(CONF_PROPERTY);
        final File configFile
            = configFileLocation == null ? null : new File(configFileLocation);
        final Config userConfig;
        if (configFile != null) {
            userConfig = ConfigFactory.parseFile(configFile);
        } else {
            userConfig = ConfigFactory.empty();
        }
        config = userConfig.withFallback(ConfigFactory.parseURL(url));
    }

    @Override
    public <T extends Object> T getConfig(String path, Class<T> clazz) {
        return ConfigBeanFactory.create(config.getConfig(path), clazz);
    }
}
