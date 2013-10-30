package org.fenixsoft.hotspot.model;

/**
 * HotSpot参数的类型，参考OpenJDK的global.hpp
 * 
 * @author icyfenix@gmail.com
 * 
 */
public enum ArgumentType {
	standard, custom, develop, develop_pd, product, product_pd, diagnostic, experimental, notproduct, manageable, product_rw, lp64_product, unknown
}
