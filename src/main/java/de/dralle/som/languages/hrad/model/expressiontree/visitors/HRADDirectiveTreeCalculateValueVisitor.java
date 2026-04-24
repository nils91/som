package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADIntegerDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADStringDirectiveValue;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbsoluteExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADComplexNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDivisionExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADFactorialExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMinusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADModuloExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMultiplicationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADNegationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPlusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPowerExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADSingleChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNode;

//For abstract nodes, nothing needs to happen (hopefully)
public class HRADDirectiveTreeCalculateValueVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveValue<?>> {

	private HRADResolveDirectiveTreeVisitor resolver = null;

	public HRADResolveDirectiveTreeVisitor getResolver() {
		return resolver;
	}

	public void setResolver(HRADResolveDirectiveTreeVisitor resolver) {
		this.resolver = resolver;
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADComplexNamedDirectiveNode node) {
		if(resolver==null) {
			throw new RuntimeException("Unresolved directive node " + node.getSourceLocation());
		}else {
			HRADAbstractDirectiveExpressionTreeNode resolvedNode = node.accept(resolver);
			return resolvedNode.accept(this);
		}		
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADAbsoluteExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChild().accept(this);
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			return new HRADIntegerDirectiveValue(Math.abs(v1Int.getValue()), node.getSourceLocation());
		}
		return new HRADIntegerDirectiveValue(v1.getValue().toString().length(), node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADAbstractDirectiveNode<?> node) {
		if(resolver==null) {
			throw new RuntimeException("Unresolved directive node " + node.getSourceLocation());
		}else {
			HRADAbstractDirectiveExpressionTreeNode resolvedNode = node.accept(resolver);
			return resolvedNode.accept(this);
		}		
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADDivisionExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADStringDirectiveValue(
						v1.getValue().toString().substring(0, v1.getValue().toString().length() / v2Int.getValue()),
						node.getSourceLocation());
			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() / v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADFactorialExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChild().accept(this);
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			return new HRADIntegerDirectiveValue(getFac(v1Int.getValue()), node.getSourceLocation());
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	private int getFac(int value) {
		return value == 0 ? 1 : value * getFac(value - 1);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADIntegerNode node) {
		return new HRADIntegerDirectiveValue(node.getValue(), node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADStringNode node) {
		return new HRADStringDirectiveValue(node.getValue(), node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADMinusExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString().replaceAll(v2.getValue().toString(), ""),
						node.getSourceLocation());
			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() - v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADModuloExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADStringDirectiveValue(
						v1.getValue().toString().substring(v1.getValue().toString().length() / v2Int.getValue()),
						node.getSourceLocation());
			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() % v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADMultiplicationExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADStringDirectiveValue(v2.getValue().toString().repeat(v1Int.getValue()),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() * v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADNegationExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChild().accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			String str = v1.getValue().toString();
			int vi = 0;
			String stri = "";
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				vi += c;

			}
			return new HRADIntegerDirectiveValue(vi, node.getSourceLocation());
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			return new HRADIntegerDirectiveValue(-v1Int.getValue(), node.getSourceLocation());
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADPlusExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString() + v2.getValue().toString(),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString() + v2.getValue().toString(),
						node.getSourceLocation());
			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADIntegerDirectiveValue(v1Int.getValue() + Integer.parseInt(v2.getValue().toString()),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() + v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADPowerExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				if (v2Int.getValue() > 0) {
					String v1str = v1.getValue().toString();
					for (int i = 0; i < v2Int.getValue(); i++) {
						v1str = i % 2 == 0 ? v1str.toUpperCase() : v1str.toLowerCase();
					}
					return new HRADStringDirectiveValue(v1str, node.getSourceLocation());
				}

			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue((int) Math.pow(v1Int.getValue(), v2Int.getValue()),
						node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}
}
