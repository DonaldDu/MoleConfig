package com.dhy.moleconfigdemo.data;

import com.dhy.moleconfig.IMoleConfig;

import java.io.Serializable;

public class AccountS implements Serializable, IMoleConfig {
    private static final long serialVersionUID = AccountS.class.getName().hashCode();
    public String name;
}
