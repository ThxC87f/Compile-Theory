package org.compiler.exp04


fun String.toNumber(): Number {
	if ("." in this) {
		return this.toDouble()
	}

	return this.toInt()
}


operator fun Number.plus(another: Number): Number {
	// double
	if (this is Double || another is Double) {
		return this.toDouble() + another.toDouble()
	}

	// float
	if (this is Float || another is Float) {
		return this.toFloat() + another.toFloat()
	}

	// long 
	if (this is Long && another is Long) {
		return this + another
	}

	return this.toInt() + another.toInt()
}

operator fun Number.minus(another: Number): Number {
	// double
	if (this is Double || another is Double) {
		return this.toDouble() - another.toDouble()
	}

	// float
	if (this is Float || another is Float) {
		return this.toFloat() - another.toFloat()
	}

	// long 
	if (this is Long && another is Long) {
		return this - another
	}

	return this.toInt() - another.toInt()
}

operator fun Number.times(another: Number): Number {
	// double
	if (this is Double || another is Double) {
		return this.toDouble() * another.toDouble()
	}

	// float
	if (this is Float || another is Float) {
		return this.toFloat() * another.toFloat()
	}

	// long 
	if (this is Long && another is Long) {
		return this * another
	}

	return this.toInt() * another.toInt()
}

operator fun Number.div(another: Number): Number {
	// double
	if (this is Double || another is Double) {
		return this.toDouble() / another.toDouble()
	}

	// float
	if (this is Float || another is Float) {
		return this.toFloat() / another.toFloat()
	}

	// long 
	if (this is Long && another is Long) {
		return this / another
	}

	return this.toInt() / another.toInt()
}




