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
package org.jbasics.parser;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jbasics.parser.annotations.AnyAttribute;
import org.jbasics.parser.annotations.AnyElement;
import org.jbasics.parser.annotations.Attribute;
import org.jbasics.parser.annotations.Comment;
import org.jbasics.parser.annotations.Content;
import org.jbasics.parser.annotations.Element;
import org.jbasics.parser.annotations.ElementBuilder;
import org.jbasics.parser.annotations.ElementImplementor;
import org.jbasics.parser.annotations.ElementImplementors;
import org.jbasics.parser.annotations.QualifiedName;
import org.jbasics.parser.invoker.AttributeInvoker;
import org.jbasics.parser.invoker.ContentInvoker;
import org.jbasics.parser.invoker.ElementInvoker;
import org.jbasics.parser.invoker.Invoker;
import org.jbasics.parser.invoker.QualifiedNameInvoker;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.builder.ReflectionBuilderFactory;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.ReflectionFactory;
import org.jbasics.types.tuples.Pair;

@SuppressWarnings("unchecked")
public class AnnotationScanner {
//	private final Map<Class<? extends Builder>, ParsingInfo> parsedBuilders = new HashMap<Class<? extends Builder>, ParsingInfo>();

	public Map<QName, ParsingInfo> scan(Class<?>... root) {
		if (root == null || root.length == 0) {
			throw new IllegalArgumentException("Null or empty parameter: builderTypes[]");
		}
		Map<QName, ParsingInfo> result = new LinkedHashMap<QName, ParsingInfo>();
		ParsingInfoBuilder builder = new ParsingInfoBuilder();
		for (Class<?> impl : root) {
			ElementImplementor elementImplementor = impl.getAnnotation(ElementImplementor.class);
			ElementImplementors elementImplementos = impl.getAnnotation(ElementImplementors.class);
			// Ok now we do have all the possible implementors Scan them
			if (elementImplementos != null) {
				for (ElementImplementor implementor : elementImplementos.value()) {
					builder.reset();
					Pair<QName, ParsingInfoBuilder> temp = scanElementImplementor(builder, implementor);
					result.put(temp.first(), temp.second().build());
				}
			}
			if (elementImplementor != null) {
				builder.reset();
				Pair<QName, ParsingInfoBuilder> temp = scanElementImplementor(builder, elementImplementor);
				result.put(temp.first(), temp.second().build());
			}
		}
		return result;
	}

	private Pair<QName, ParsingInfoBuilder> scanElementImplementor(ParsingInfoBuilder builder,
			ElementImplementor implementor) {
		QName name = new QName(implementor.namespace(), implementor.localName());
		Class<?> clazz = implementor.builderClass();
		return new Pair<QName, ParsingInfoBuilder>(name, scan(builder, clazz));
	}

	private <T> ParsingInfoBuilder scan(ParsingInfoBuilder builder, Class<T> implClass) {
		ElementBuilder temp = implClass.getAnnotation(ElementBuilder.class);
		Class<? extends Builder> builderType;
		Factory<? extends Builder> factory;
		if (temp != null) {
			builderType = temp.value();
			factory = ReflectionFactory.create(builderType);
		} else {
			ReflectionBuilderFactory<T> tempFactory = ReflectionBuilderFactory.createFactory(implClass);
			builderType = tempFactory.getBuilderClass();
			factory = tempFactory;
		}
		return scanType(builder, builderType, factory);
	}

	private ParsingInfoBuilder scanType(ParsingInfoBuilder builder, Class<? extends Builder> builderType,
			Factory<? extends Builder> builderFactory) {
		assert builderType != null;
		builder.setBuilderType(builderType);
		if (builderFactory != null) {
			builder.setBuilderFactory(builderFactory);
		} else {
			builder.setBuilderFactory(ReflectionFactory.create(builderType));
		}
		for (Method m : builderType.getMethods()) {
			QualifiedName qualifiedName = m.getAnnotation(QualifiedName.class);
			Attribute directAttribute = m.getAnnotation(Attribute.class);
			AnyAttribute anyAttribute = m.getAnnotation(AnyAttribute.class);
			Element directElement = m.getAnnotation(Element.class);
			AnyElement anyElement = m.getAnnotation(AnyElement.class);
			Content contentElement = m.getAnnotation(Content.class);
			Comment commentElement = m.getAnnotation(Comment.class);
			if (isMoreThanNotNull(qualifiedName, directAttribute, anyAttribute, directElement, anyElement,
					contentElement, commentElement)) {
				throw new IllegalArgumentException("Cannot have more than one type of annotaion on method "
						+ m.getName());
			}
			if (qualifiedName != null) {
				builder.setQualifiedName(QualifiedNameInvoker.createInvoker(builderType, m));
			} else if (contentElement != null) {
				processContent(builder, builderType, m, contentElement);
			} else if (commentElement != null) {
				processComment(builder, builderType, m, commentElement);
			} else if (directAttribute != null || anyAttribute != null) {
				processAttribute(builder, builderType, m, directAttribute, anyAttribute);
			} else if (directElement != null || anyElement != null) {
				processElement(builder, m, directElement, anyElement);
			}
		}
		return builder;
	}

	private void processContent(ParsingInfoBuilder builder, Class<? extends Builder> builderType, Method m,
			Content contentElement) {
		if (contentElement.mixed()) {
			throw new UnsupportedOperationException("Mixed content is not yet implemented");
		} else {
			builder.setContentInvoker(ContentInvoker.createInvoker(builderType, m));
		}
	}

    private void processComment(ParsingInfoBuilder builder, Class<? extends Builder> builderType, Method m, Comment commentElement) {
    	builder.setCommentInvoker(ContentInvoker.createInvoker(builderType, m));
    }

	private ParsingInfoBuilder processAttribute(ParsingInfoBuilder builder, Class<? extends Builder> builderType,
			Method m, Attribute directAttribute, AnyAttribute anyAttribute) {
		assert m != null && (directAttribute != null || anyAttribute != null);
		if (directAttribute != null) {
			QName qualifiedName = new QName(directAttribute.namespace(), directAttribute.name());
			builder.addAttribute(qualifiedName, AttributeInvoker.createInvoker(builderType, m));
		} else if (anyAttribute != null) {
			builder.setAnyAttribute(AttributeInvoker.createInvoker(builderType, m));
		}
		return builder;
	}

	private ParsingInfoBuilder processElement(ParsingInfoBuilder builder, Method m, Element directElement,
			AnyElement anyElement) {
		assert m != null && (directElement != null || anyElement != null);
		Class<?>[] params = m.getParameterTypes();
		Class<?> type = null;
		if (params.length == 1) {
			type = params[0];
		} else {
			throw new IllegalArgumentException("Wrong signature for element method");
		}
		// Now we have the type. We need to figure out the builder of this type. There are multiple
		// ways to solve this.
		// 1. The static newBuilder() methods should return an instance of the builder therefor the
		// return type would be the builder class
		// 2. Annotate the builder class at the instance type with a BuilderAnnotation
		// 3. Annotate the builder in the element description
		Class<? extends Builder> subBuilderType = null;
		Factory<? extends Builder> subBuilderFactory = null;
		try {
			ElementBuilder temp = m.getAnnotation(ElementBuilder.class);
			if (temp == null) {
				temp = type.getAnnotation(ElementBuilder.class);
			}
			if (temp != null) {
				subBuilderType = temp.value();
				subBuilderFactory = ReflectionFactory.create(subBuilderType);
			} else {
				ReflectionBuilderFactory<?> tempFactory = ReflectionBuilderFactory.createFactory(type);
				subBuilderType = tempFactory.getBuilderClass();
				subBuilderFactory = tempFactory;
			}
		} catch (RuntimeException e) {
			subBuilderType = SimpleTypeBuilder.class;
			subBuilderFactory = SimpleTypeBuilder.BuilderFactory.createFactory(type);
		}
		ParsingInfo subInfo = null;
		if (subBuilderType == builder.getBuilderType()) {
			subInfo = ParsingInfo.SELF;
		} else {
			subInfo = scanType(new ParsingInfoBuilder(), subBuilderType, subBuilderFactory).build();
		}
		// TODO: The builderType cannot be right here can it? It should be the builder on which the
		// element to add right? That however is another
		// one than the builder of the element we now want to add! So the parsing info could
		// possible have the right builder set
		Pair<ParsingInfo, Invoker<?, ?>> x = new Pair<ParsingInfo, Invoker<?, ?>>(subInfo, ElementInvoker
				.createInvoker(builder.getBuilderType(), type, m));
		if (directElement != null) {
			QName qualifiedName = new QName(directElement.namespace(), directElement.name());
			builder.addElement(qualifiedName, x);
		} else if (anyElement != null) {
			builder.setAnyElement(x);
		}
		return builder;
	}

	private boolean isMoreThanNotNull(Object... elements) {
		boolean foundOne = false;
		for (Object temp : elements) {
			if (temp != null) {
				if (foundOne) {
					return true;
				} else {
					foundOne = true;
				}
			}
		}
		return false;
	}

}
