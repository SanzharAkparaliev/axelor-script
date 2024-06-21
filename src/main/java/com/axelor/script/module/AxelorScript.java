package com.axelor.script.module;

import com.axelor.app.AxelorModule;
import com.axelor.script.service.DictionaryService;
import com.axelor.script.service.FieldService;
import com.axelor.script.service.PermissionService;
import com.axelor.script.service.impl.DictionaryServiceImpl;
import com.axelor.script.service.impl.FieldServiceImpl;
import com.axelor.script.service.impl.PermissionServiceImpl;

public class AxelorScript extends AxelorModule {
    @Override
    protected void configure() {
        bind(PermissionService.class).to(PermissionServiceImpl.class);
        bind(FieldService.class).to(FieldServiceImpl.class);
        bind(DictionaryService.class).to(DictionaryServiceImpl.class);
    }
}
