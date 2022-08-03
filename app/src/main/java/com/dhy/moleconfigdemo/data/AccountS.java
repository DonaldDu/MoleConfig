package com.dhy.moleconfigdemo.data;

import com.dhy.moleconfig.KeepMoleConfig;

import java.io.Serializable;

public class AccountS implements Serializable, KeepMoleConfig {
    private static final long serialVersionUID = AccountS.class.getName().hashCode();
    public String name;
}
