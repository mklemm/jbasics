/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.math.impl;

import java.math.BigDecimal;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.strategies.NaturalLogarithmAlgorithmStrategy;

public class LogNaturalFunctionIrationalNumber extends BigDecimalIrationalNumber {
	private static final AlgorithmStrategy<BigDecimal> STRATEGY = new NaturalLogarithmAlgorithmStrategy();
	/**
	 * The logarithm naturals of two as cached value.
	 */
	public static final IrationalNumber<BigDecimal> LN2 = new LogNaturalFunctionIrationalNumber(MathImplConstants.TWO);
	/**
	 * The logarithm naturals of ten as cached value.
	 */
	public static final IrationalNumber<BigDecimal> LN10 = new LogNaturalFunctionIrationalNumber(BigDecimal.TEN);

	public static IrationalNumber<BigDecimal> valueOf(BigDecimal x) {
		if (ContractCheck.mustNotBeNull(x, "x").signum() == 0) {
			throw new ArithmeticException("Logarithm of zero or negative number cannot be calculated");
		}
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return MathImplConstants.IRATIONAL_ZERO;
		} else if (MathImplConstants.TWO.compareTo(x) == 0) {
			return LN2;
		} else if (BigDecimal.TEN.compareTo(x) == 0) {
			return LN10;
		} else {
			return new LogNaturalFunctionIrationalNumber(x);
		}
	}

	private LogNaturalFunctionIrationalNumber(BigDecimal x) {
		super(STRATEGY, x);
		assert x != null && x.signum() > 0;
	}

}
