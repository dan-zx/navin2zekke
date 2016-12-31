/*
 * Copyright 2016-2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zekke.navin2zekke.dao.navin.impl;

import com.zekke.navin2zekke.domain.navin.Path;
import com.zekke.navin2zekke.test.groups.DatabaseGroupSupport;

import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class PathActiveJdbcDaoTest {

    @Test(groups = "database")
    public void shouldFindByPlace1Id() {
        PathActiveJdbcDao pathDao = DatabaseGroupSupport.getInstance(PathActiveJdbcDao.class);
        List<Path> paths = pathDao.findByPlace1Id(1);
        assertThat(paths).isNotNull().isNotEmpty().hasSize(2).extracting("place1.id", "place2.id", "distance").containsOnly(tuple(1, 2, 120000.0), tuple(1, 3, 110000.0));
    }
}
