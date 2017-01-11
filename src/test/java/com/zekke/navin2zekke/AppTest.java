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
package com.zekke.navin2zekke;

import com.zekke.navin2zekke.database.DatabaseHelper;
import com.zekke.navin2zekke.service.DomainTranslatorService;
import com.zekke.navin2zekke.service.OutputService;
import com.zekke.navin2zekke.test.groups.MockGroupSupport;

import org.mockito.InOrder;
import org.testng.annotations.Test;

import static java.util.Collections.emptySet;
import static org.mockito.Mockito.anyCollection;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class AppTest {

    @Test(groups = "mock")
    public void testBehavior() {
        DatabaseHelper databaseHelperMock = MockGroupSupport.getInstance(DatabaseHelper.class);
        DomainTranslatorService domainTranslatorServiceMock = MockGroupSupport.getInstance(DomainTranslatorService.class);
        OutputService outputServiceMock = MockGroupSupport.getInstance(OutputService.class);

        doNothing().when(databaseHelperMock).init();
        doNothing().when(databaseHelperMock).destroy();
        when(domainTranslatorServiceMock.translateNavinToZekke()).thenReturn(emptySet());
        doNothing().when(outputServiceMock).write(anyCollection());

        new App(MockGroupSupport.getInjector()).run();

        InOrder inOrder = inOrder(databaseHelperMock, domainTranslatorServiceMock, outputServiceMock);
        inOrder.verify(databaseHelperMock).init();
        inOrder.verify(domainTranslatorServiceMock).translateNavinToZekke();
        inOrder.verify(outputServiceMock).write(emptySet());
        inOrder.verify(databaseHelperMock).destroy();
        inOrder.verifyNoMoreInteractions();

        reset(databaseHelperMock, domainTranslatorServiceMock, outputServiceMock);
    }
}
