package ru.sladkov.services.impl;

import ru.sladkov.appcontainer.annotations.Reference;
import ru.sladkov.services.NotRegisteredService;
import ru.sladkov.services.UnresolvedService;

//@AppComponent(name = "Service with unresolved dependency", interfaze = UnresolvedService.class)
public class UnresolvedServiceImpl implements UnresolvedService {

    @Reference
    NotRegisteredService notRegisteredService;
}
