package com.Beendo.Utils;

public enum TransactionType {

	 Provider(1),
	 Practise(2);
    private final int value;

    private TransactionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
