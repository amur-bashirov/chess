package service;

import dataaccess.ClearAccess;
import DataObjects.DataAccessException;

public class ClearService {
    private final ClearAccess clearAccess;

    public ClearService( ClearAccess clear) {
        this.clearAccess = clear;
    }

    public void clear() throws DataAccessException {
         clearAccess.clear();
    }
}
