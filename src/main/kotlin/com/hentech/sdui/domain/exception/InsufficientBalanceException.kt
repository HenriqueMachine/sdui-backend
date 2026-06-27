package com.hentech.sdui.domain.exception

import java.math.BigDecimal

class InsufficientBalanceException(balance: BigDecimal, amount: BigDecimal) :
    RuntimeException("Insufficient balance: available $balance, requested $amount")
