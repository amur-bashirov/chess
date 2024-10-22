package service;

import dataaccess.ClearAccess;
import dataaccess.MemoryClearDAO;

public class ClearService {
    private final ClearAccess clearAccess;

    public ClearService( ClearAccess clear) {
        this.clearAccess = clear;
    }

    public void clear() {
         clearAccess.clear();
    }
}
