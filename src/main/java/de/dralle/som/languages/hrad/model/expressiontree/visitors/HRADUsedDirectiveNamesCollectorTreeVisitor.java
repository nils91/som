package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADSingleChildExpressionNode;

//For abstract nodes, nothing needs to happen
//This visitor does not avoid modifying the tree it visits by default. Either clone the tree first or use one of the constructors with the clone param
public class HRADUsedDirectiveNamesCollectorTreeVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<Collection<String>> {

	@Override
	public Collection<String> postVisit(
			HRADDirectiveExpressionTreeVisitorInterface<Collection<String>> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node, Collection<String> returnyValue) {
		return returnyValue == null ? new ArrayList<String>() : returnyValue;
	}

	@Override
	public Collection<String> visit(HRADStringNamedDirectiveNode node) {
		return Arrays.asList(node.getDirectiveName());
	}

	@Override
	public Collection<String> visit(HRADDualChildExpressionNode node) {
		List<String> r = new ArrayList<String>();
		for (HRADAbstractDirectiveExpressionTreeNode string : node.getChilds()) {
			r.addAll(string.accept(this));
		}
		return r;
	}

	@Override
	public Collection<String> visit(HRADSingleChildExpressionNode node) {
		return node.getChild().accept(this);
	}

}
