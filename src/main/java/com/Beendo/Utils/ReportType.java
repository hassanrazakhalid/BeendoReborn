package com.Beendo.Utils;

public enum ReportType {

	ReportTypePractise,
	ReportTypeProvider,
	ReportTypeTransaction;
	
	public static ReportType fromInteger(int x) {
        switch(x) {
        case 1:
            return ReportTypePractise;
        case 2:
            return ReportTypeProvider;
        case 3:
        	return ReportTypeTransaction;
        }
        return null;
    }
}
