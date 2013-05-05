package logoparsing;

import logogui.Traceur;
import logoparsing.LogoParser.AffectationExpressionContext;
import logoparsing.LogoParser.AndContext;
import logoparsing.LogoParser.ArithmeticExpressionIntContext;
import logoparsing.LogoParser.ArithmeticExpressionLoopContext;
import logoparsing.LogoParser.ArithmeticExpressionVarContext;
import logoparsing.LogoParser.AvContext;
import logoparsing.LogoParser.BcContext;
import logoparsing.LogoParser.BlockContext;
import logoparsing.LogoParser.BoolContext;
import logoparsing.LogoParser.DivContext;
import logoparsing.LogoParser.ElseBlockContext;
import logoparsing.LogoParser.EqContext;
import logoparsing.LogoParser.FccContext;
import logoparsing.LogoParser.FposContext;
import logoparsing.LogoParser.IfExpressionContext;
import logoparsing.LogoParser.InfContext;
import logoparsing.LogoParser.InfEqContext;
import logoparsing.LogoParser.LcContext;
import logoparsing.LogoParser.MulContext;
import logoparsing.LogoParser.OrContext;
import logoparsing.LogoParser.ParenthesisContext;
import logoparsing.LogoParser.ProcedureCallArgsContext;
import logoparsing.LogoParser.ProcedureCallContext;
import logoparsing.LogoParser.ProcedureCallInstructionContext;
import logoparsing.LogoParser.ProcedureDeclarationContext;
import logoparsing.LogoParser.ProcedureDeclarationInstructionContext;
import logoparsing.LogoParser.ProcedureListeArgsContext;
import logoparsing.LogoParser.RandContext;
import logoparsing.LogoParser.ReContext;
import logoparsing.LogoParser.RepeatExpressionContext;
import logoparsing.LogoParser.SubContext;
import logoparsing.LogoParser.SumContext;
import logoparsing.LogoParser.SupContext;
import logoparsing.LogoParser.SupEqContext;
import logoparsing.LogoParser.TdContext;
import logoparsing.LogoParser.TgContext;
import logoparsing.LogoParser.VeContext;
import logoparsing.LogoParser.WhileExpressionContext;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class LogoTreeVisitor extends LogoBaseVisitor<Value> {
	Traceur traceur;
	ParseTreeProperty<Value> atts 		= new ParseTreeProperty<Value>();
	VarDictionary 			m_dico 		= new VarDictionary();
	FuncDictionary 			m_funcDico 	= new FuncDictionary();
	
	int						m_iProcedureCurrentArgId;
	
	boolean 				m_bFunctionFirstVisit;
	boolean					m_bIsInFunction;
	
	String 					m_currentFunctionName;

	public LogoTreeVisitor() {
		super();
	}
	
	public void initialize(java.awt.Graphics g) {
	      traceur = new Traceur();
	      traceur.setGraphics(g);
    }
	
	public Value setAttValue(ParseTree node, int value) { 
		Value val = new Value(value);
		atts.put(node, val);
		return val;
	}
	
	public Value setAttValue(ParseTree node, boolean value) { 
		Value val = new Value(value);
		atts.put(node, val);
		return val;
	}
	
	public Value setAttValue(ParseTree node, Value value) { 
		atts.put(node, value);
		return value;
	}
	
	public Value getAttValue(ParseTree node) { return atts.get(node); }
	
	@Override
	public Value visitAv(AvContext ctx) {
		visitChildren(ctx);
		traceur.avance(getAttValue(ctx.arithmeticExpression()).getInt());
		return new Value();
	}
	
	@Override
	public Value visitRe(ReContext ctx) {
		visitChildren(ctx);
		traceur.recule(getAttValue(ctx.arithmeticExpression()).getInt());
		return new Value();
	}

	@Override
	public Value visitTd(TdContext ctx) {
		visitChildren(ctx);
		traceur.td(getAttValue(ctx.arithmeticExpression()).getInt());
		return new Value();
	}

	@Override
	public Value visitTg(TgContext ctx) {
		visitChildren(ctx);
		traceur.tg(getAttValue(ctx.arithmeticExpression()).getInt());
		return new Value();
	}

	@Override
	public Value visitFpos(FposContext ctx) {
		visitChildren(ctx);
		traceur.fPos(getAttValue(ctx.arithmeticExpression(0)).getInt(), getAttValue(ctx.arithmeticExpression(1)).getInt());
		return new Value();
	}

	/**
	 * Baisser Crayon
	 */
	@Override
	public Value visitBc(BcContext ctx) {
		traceur.bc();
		return new Value();
	}
	
	/**
	 * Lever Crayon
	 */
	@Override
	public Value visitLc(LcContext ctx) {
		traceur.lc();
		return new Value();
	}

	@Override
	public Value visitVe(VeContext ctx) {
		traceur.ve();
		return new Value();
	}

	@Override
	public Value visitFcc(FccContext ctx) {
		visitChildren(ctx);
		traceur.setColor(getAttValue(ctx.arithmeticExpression()).getInt());
		return new Value();
	}
	
	/*
	 * Arithmetic Expressions
	 */

	@Override
	public Value visitArithmeticExpressionInt(ArithmeticExpressionIntContext ctx) {
		visitChildren(ctx);
		String intText = ctx.INT().getText();
		Value val = new Value(intText);
		setAttValue(ctx.INT(), val.getInt());
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitSub(SubContext ctx) {
		visitChildren(ctx);
		int val = getAttValue(ctx.arithmeticExpression(0)).getInt() - getAttValue(ctx.arithmeticExpression(1)).getInt();
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitDiv(DivContext ctx) {
		visitChildren(ctx);
		int val = getAttValue(ctx.arithmeticExpression(0)).getInt() / getAttValue(ctx.arithmeticExpression(1)).getInt();
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitSum(SumContext ctx) {
		visitChildren(ctx);
		int val = getAttValue(ctx.arithmeticExpression(0)).getInt() + getAttValue(ctx.arithmeticExpression(1)).getInt();
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitMul(MulContext ctx) {
		visitChildren(ctx);
		int val = getAttValue(ctx.arithmeticExpression(0)).getInt() * getAttValue(ctx.arithmeticExpression(1)).getInt();
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitRand(RandContext ctx) {
		visitChildren(ctx);
		int val = (int)(Math.random() * (getAttValue(ctx.arithmeticExpression()).getInt() + 1));
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitParenthesis(ParenthesisContext ctx) {
		visitChildren(ctx);
		int val = getAttValue(ctx.arithmeticExpression()).getInt();
		return setAttValue(ctx, val);
	}
	
	/*
	 * Boolean Expressions
	 */
	
	@Override
	public Value visitSup(SupContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.arithmeticExpression(0)).getInt() > getAttValue(ctx.arithmeticExpression(1)).getInt());
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitInfEq(InfEqContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.arithmeticExpression(0)).getInt() <= getAttValue(ctx.arithmeticExpression(1)).getInt());
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitInf(InfContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.arithmeticExpression(0)).getInt() < getAttValue(ctx.arithmeticExpression(1)).getInt());
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitSupEq(SupEqContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.arithmeticExpression(0)).getInt() >= getAttValue(ctx.arithmeticExpression(1)).getInt());
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitEq(EqContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.arithmeticExpression(0)).getInt() == getAttValue(ctx.arithmeticExpression(1)).getInt());
		return setAttValue(ctx, val);
	}
	
	@Override
	public Value visitBool(BoolContext ctx) {
		String boolText = ctx.BOOL().getText();
		Value val = new Value(boolText);
		return setAttValue(ctx, val);
	}
	
	@Override
	public Value visitAnd(AndContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.booleanExpression(0)).getBool() && getAttValue(ctx.booleanExpression(1)).getBool());
		return setAttValue(ctx, val);
	}
	
	@Override
	public Value visitOr(OrContext ctx) {
		visitChildren(ctx);
		boolean val = (getAttValue(ctx.booleanExpression(0)).getBool() || getAttValue(ctx.booleanExpression(1)).getBool());
		return setAttValue(ctx, val);
	}
	
	/*
	 * If Expression
	 */
	
	@Override
	public Value visitIfExpression(IfExpressionContext ctx) {
		Value val = visit(ctx.booleanExpression());
		if(val.getBool()) {
			visit(ctx.block());
		} else {
			visit(ctx.elseBlock());
		}
		
		return setAttValue(ctx, val); 
	}
	
	@Override
	public Value visitElseBlock(ElseBlockContext ctx) {
		visitChildren(ctx);
		return new Value();
	}
	
	/*
	 * Block
	 */
	
	@Override
	public Value visitBlock(BlockContext ctx) {
		visitChildren(ctx);
		return new Value();
	}
	
	/*
	 * Repeat
	 */
	
	@Override
	public Value visitRepeatExpression(RepeatExpressionContext ctx) {
		int nbRepeat = visit(ctx.arithmeticExpression()).getInt();
		for(int i = 0; i < nbRepeat; ++i) {
			setAttValue(ctx, i);
			visit(ctx.block());
		}
		return new Value();
	}
	
	/*
	 * While
	 */
	
	@Override
	public Value visitWhileExpression(WhileExpressionContext ctx) {
		int i = 0;
		while(visit(ctx.booleanExpression()).getBool()) {
			setAttValue(ctx, i++);
			visit(ctx.block());
		}
		return new Value();
	}
	
	/*
	 * Variables
	 */
	
	@Override
	public Value visitAffectationExpression(AffectationExpressionContext ctx) {
		visitChildren(ctx);
		int val 	= getAttValue(ctx.arithmeticExpression()).getInt();
		String var  = ctx.ID().getText();
		m_dico.put(var, val);
		return setAttValue(ctx, val);
	}

	@Override
	public Value visitArithmeticExpressionVar(ArithmeticExpressionVarContext ctx) {
		visitChildren(ctx);
		String varText = ctx.ID().getText();
		try {
			Value val;
			if(m_bIsInFunction) {
				val = new Value(m_funcDico.get(m_currentFunctionName).getArgValue(varText));
			} else {
				val = new Value(m_dico.get(varText));					
			}
			return setAttValue(ctx, val);
		} catch (Exception e) {
			return new Value();
		}
	}

	/*
	 * LOOP variable
	 */
	
	@Override
	public Value visitArithmeticExpressionLoop(ArithmeticExpressionLoopContext ctx) {
		ParserRuleContext loopCtx = ctx;
		while(!(loopCtx instanceof RepeatExpressionContext) && !(loopCtx instanceof WhileExpressionContext)) {
			loopCtx = loopCtx.getParent();
		}
		return setAttValue(ctx, getAttValue(loopCtx).getInt());
	}
	
	/*
	 * Procedures
	 */
	
	@Override
	public Value visitProcedureDeclarationInstruction(ProcedureDeclarationInstructionContext ctx) {
		visitChildren(ctx);
		return new Value();
	}

	@Override
	public Value visitProcedureCallInstruction(ProcedureCallInstructionContext ctx) {
		visitChildren(ctx);
		return new Value();
	}
	
	@Override
	public Value visitProcedureDeclaration(ProcedureDeclarationContext ctx) {
		
		// Just count the arguments
		m_bFunctionFirstVisit = true;
		int nbArgs = visit(ctx.procedureListeArgs()).getInt();
		
		String key = ctx.ID().getText();
		FuncDictionaryEntry value = new FuncDictionaryEntry(ctx.liste_instructions(), nbArgs);
		
		m_funcDico.put(key, value);
		
		// Add the arguments to the dictionary
		m_bFunctionFirstVisit = false;
		visit(ctx.procedureListeArgs()).getInt();
		
		return new Value();
	}

	@Override
	public Value visitProcedureListeArgs(ProcedureListeArgsContext ctx) {
		
		if(ctx.procedureListeArgs() == null) {
			return new Value(0);			
		}
		
		if(!m_bFunctionFirstVisit) {
			// Store the actual arg
			ParserRuleContext parentCtx = ctx;
			while(!(parentCtx instanceof ProcedureDeclarationContext)) {
				parentCtx = parentCtx.getParent();
			}
			
			String funcName = ((ProcedureDeclarationContext)parentCtx).ID().getText();
			try {
				m_funcDico.get(funcName).addArgument(ctx.ID().getText(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int nbArgs = visit(ctx.procedureListeArgs()).getInt();
		return new Value(nbArgs + 1);
	}

	@Override
	public Value visitProcedureCall(ProcedureCallContext ctx) {
		String funcName = ctx.ID().getText();
		try {
			FuncDictionaryEntry entry = m_funcDico.get(funcName);
			
			// Affect args values
			if(entry.getArgsNumber() > 0) {
				m_iProcedureCurrentArgId = 0;
				visit(ctx.procedureCallArgs());
			}
			
			ParserRuleContext procCtx = entry.getParserRuleContext();
			m_bIsInFunction = true;
			m_currentFunctionName = funcName;
			visit(procCtx);
			m_bIsInFunction = false;
			m_currentFunctionName = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Value();
	}

	@Override
	public Value visitProcedureCallArgs(ProcedureCallArgsContext ctx) {
		
		// Store the actual arg
		ParserRuleContext parentCtx = ctx;
		while(!(parentCtx instanceof ProcedureCallContext)) {
			parentCtx = parentCtx.getParent();
		}
		
		String funcName = ((ProcedureCallContext)parentCtx).ID().getText();
		try {
			FuncDictionaryEntry fentry = m_funcDico.get(funcName);
			if(m_iProcedureCurrentArgId < fentry.getArgsNumber()) {
				Integer[] values = fentry.getArgsValues();
				values[m_iProcedureCurrentArgId++] = visit(ctx.arithmeticExpression()).getInt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Visit next arg
		if(ctx.procedureCallArgs() != null) {
			visit(ctx.procedureCallArgs());
		}
		
		return new Value();
	}
	
	
	
}
