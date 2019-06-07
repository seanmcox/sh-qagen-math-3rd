/**
 * 
 */
package com.shtick.apps.sh.qagen.math.third;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import com.shtick.apps.sh.core.Driver;
import com.shtick.apps.sh.core.Question;
import com.shtick.apps.sh.core.QuestionDimension;
import com.shtick.apps.sh.core.Subject;
import com.shtick.apps.sh.core.SubjectQuestionGenerator;
import com.shtick.apps.sh.core.UserID;

/**
 * @author sean.cox
 *
 */
public class ThirdGradeMathQuestionGenerator implements SubjectQuestionGenerator {
	private static Random RANDOM = new Random();
	private static final Subject subject = new Subject("com.shtick.math.3rd");
	private static final HashMap<String,String> dimensionDescriptions = new HashMap<>();
	static {
		// TODO Set descriptions.
	}

	/* (non-Javadoc)
	 * @see com.shtick.apps.sh.core.SubjectQuestionGenerator#getSubject()
	 */
	@Override
	public Subject getSubject() {
		return subject;
	}

	/* (non-Javadoc)
	 * @see com.shtick.apps.sh.core.SubjectQuestionGenerator#generateQuestions(com.shtick.apps.sh.core.Driver, com.shtick.apps.sh.core.UserID, int)
	 */
	@Override
	public Collection<Question> generateQuestions(Driver driver, UserID userID, int count) {
		if(count<=0)
			throw new IllegalArgumentException("One ore more questions must be requested.");
		ArrayList<Question> retval = new ArrayList<>();
		for(int i=0;i<count;i++)
			retval.add(generateQuestion());
		return retval;
	}

	private Question generateQuestion(){
		int type = RANDOM.nextInt(4);
		int a,b,c;
		boolean isStory = RANDOM.nextBoolean();
		boolean findC = RANDOM.nextBoolean();
		switch(type){
		case 0:// Addition
			a = RANDOM.nextInt(1000);
			b = RANDOM.nextInt(1000);
			c = a+b;
			break;
		case 1:// Subtraction
			a = RANDOM.nextInt(1000);
			b = RANDOM.nextInt(1000);
			int t = Math.min(a,b);
			a = Math.max(a, b);
			b = t;
			c = a-b;
			break;
		case 2:// Multiplication
			a = RANDOM.nextInt(100);
			b = RANDOM.nextInt(9)+1;
			c = a*b;
			break;
		default:// Division
			b = RANDOM.nextInt(9)+1;
			c = RANDOM.nextInt(20);
			a = b*c;
			break;
		}
		return generateQuestion(a,b,c,type,isStory, findC);
	}
	
	private Question generateQuestion(int a, int b, int c, int type, boolean isStory, boolean findC){
		String prompt;
		String promptType = "text/plain";
		String answerPrompt = findC?"What is the answer?":"What is the missing value?";
		String answerPromptType = "text/plain";
		String answerValue = ""+(findC?c:b);
		HashMap<String,Float> dimensions = new HashMap<>();
		if(!isStory){
			switch(type){
			case 0:// Addition
				if(findC)
					prompt = ""+a+" + "+b+" = ?";
				else
					prompt = ""+a+" + ? = "+c;
				break;
			case 1:// Subtraction
				if(findC)
					prompt = ""+a+" - "+b+" = ?";
				else if(RANDOM.nextBoolean())
					prompt = "? - "+b+" = "+c;
				else
					prompt = ""+a+" - ? = "+c;
				break;
			case 2:// Multiplication
				if(findC){
					prompt = ""+a+" \u00D7 "+b+" = ?";
				}
				else{
					prompt = "? \u00D7 "+b+" = "+c;
					answerValue = ""+a;
				}
				break;
			default:// Division
				if(findC){
					prompt = ""+a+" \u00F7 "+b+" = ?";
				}
				else if(RANDOM.nextBoolean()){
					prompt = "? \u00F7 "+b+" = "+c;
					answerValue = ""+a;
				}
				else{
					prompt = ""+a+"\u00F7 ? = "+c;
				}
				break;
			}
		}
		else{
			String name = getRandomName();
			String name2 = getRandomName();
			while(name2.equals(name))
				name2=getRandomName();
			switch(type){
			case 0:// Addition
				if(findC){
					prompt = name+" had "+a+" fish in the freezer on Tuesday. On Wednesday "+name+" caught "+b+" more.";
					answerPrompt = "How many fish does "+name+" have now?";
				}
				else{
					prompt = name+" had "+a+" fish in the freezer on Tuesday. On Wednesday "+name+" caught more. Now "+name+" has "+c+" fish in the freezer.";
					answerPrompt = "How many fish did "+name+" catch on Wednesday?";
				}
				break;
			case 1:// Subtraction
				if(findC){
					prompt = name+" had "+a+" fish in the freezer on Tuesday. "+name+" sold "+b+" fish on Wednesday.";
					answerPrompt = "How many fish does "+name+" have now?";
				}
				else if(RANDOM.nextBoolean()){
					prompt = name+" had "+a+" fish in the freezer on Tuesday. "+name+" sold fish on Wednesday and now has "+c+" fish.";
					answerPrompt = "How many fish did "+name+" sell on Wednesday?";
				}
				else{
					prompt = name+" had some fish in the freezer on Tuesday. "+name+" sold "+b+" fish on Wednesday and now has "+c+" fish.";
					answerPrompt = "How many fish did "+name+" have on Tuesday?";
					answerValue = ""+a;
				}
				break;
			case 2:// Multiplication
				if(findC){
					prompt = name+" had "+a+" fish. "+name+" cut each fish into "+b+" "+((b>1)?"pieces":"piece")+".";
					answerPrompt = "How many pieces of fish did "+name+" have?";
				}
				else{
					prompt = name+" had some fish. "+name+" cut each fish into "+b+" "+((b>1)?"pieces":"piece")+" and ended up with "+c+" pieces.";
					answerPrompt = "How many fish did "+name+" cut up?";
					answerValue = ""+a;
				}
				break;
			default:// Division
				if(findC){
					prompt = name+" had "+a+" fish in the freezer. "+name+" was giving the fish to "+((b>1)?"each of "+name+"'s":"")+" "+b+" "+((b>1)?"friends":"friend")+".";
					answerPrompt = "How many fish did each friend get?";
				}
				else if(RANDOM.nextBoolean()){
					prompt = name+" had "+a+" fish in the freezer. "+name+" was giving the fish to "+name+"'s friends. Each friend got "+c+" fish.";
					answerPrompt = "How many friends does "+name+" have?";
				}
				else{
					prompt = name+" had some fish in the freezer. "+name+" was giving all of the fish to "+((b>1)?"each of "+name+"'s":"")+" "+b+" "+((b>1)?"friends":"friend")+". "+name+" had enough fish to give "+c+" fish to each friend with none left over";
					answerPrompt = "How many fish did "+name+" have?";
					answerValue = ""+a;
				}
				break;
			}
		}
		return new Question(prompt,promptType,answerPrompt,answerPromptType,answerValue,dimensions,4);
	}
	
	private static final String[] NAMES = new String[]{
			"Ephraim",
			"Jasher",
			"Math",
			"Enoch",
			"Claire",
			"Cassey",
			"Sean",
			"Susan",
			"Ronald",
			"David",
			"Nicholas",
			"Nathan",
			"Ronni",
			"Carlos",
			"Kim",
			"Kelly",
			"Crystal",
			"Chelsea",
			"Scott",
			"Elthon",
			"Alison",
			"Alice",
			"Nishelle",
			"Lillian",
			"Dallin",
			"Edison",
			"Gladys",
			"Bartolo",
			"Andrew",
			"Bulleta",
			"Adison",
			"Cora",
			"Justin",
			"Solace",
			"Slender Man",
			"Freddy",
			"Steve",
			"Oscar",
			"Jared",
			"Shia",
			"Pam",
			"John",
			"Jill",
			"Jane",
			"Amber"
	};
	
	private String getRandomName(){
		return NAMES[RANDOM.nextInt(NAMES.length)];
	}
	
	/**
	 * Numbers through 1000
	 * - Compare and Order
	 * - Rounding
	 * Odd and Even
	 * Addition
	 * Subtraction
	 * Story Problems (Dimensions below)
	 * - Combine (Number sentence described is addition. Missing quantity may or may not be the sum.)
	 * - Compare (Number sentence described is subtraction. Missing quantity may or may not be the difference.)
	 * - Addition
	 * - Subtraction
	 * Multiplication
	 * - Facts
	 * - More Complicated
	 * - Commutative Property (Multiplication works forwards and backwards.)
	 * - Associative Property (Sequential multiplications can be done contrary to order of operations.)
	 * Story Problems
	 * - Multiplication
	 * -- Groups
	 * -- Measures
	 * - Division
	 * Division
	 * - Symbols
	 * - Opposite of multiplication
	 * Algebraic Thinking
	 * - Translating Story Problems to Number Sentences
	 * - Comparing Expressions
	 * - Missing Symbols
	 * - Missing Values
	 * - Vocab: Operator names vs. operation verbs (!"plussing" or "timesing")
	 * Number Patterns
	 * - Counting by n
	 * Geometry
	 * - Angles
	 * -- >, <, =Right Angles
	 * -- Vocabulary: Right, Acute, Obtuse
	 * - Polygon Identification: 3-10
	 * - Triangles
	 * -- Vocab: Isosceles, Equilateral, Right
	 * - Parallelograms
	 * -- Vocab: Parallelogram, Rectangle, Rhombus, Square, (Quadrilateral?)
	 * - Perimeter
	 * -- Add up perimeter
	 * -- Using symmetries of square/rectangle to help find perimeter
	 * -- Identify length of missing side
	 * - Area
	 * -- Square/rectangle area
	 * -- Composite rectangle areas
	 * Order of Operations
	 * Multi-Step Problems
	 * Fractions
	 * - Representing Fractions
	 * - Vocab: Half, Quarter, Ordinals
	 * - Equivalent Fractions
	 * - Ordering Fractions
	 * -- like denominators
	 * -- like numerators
	 * - Recognize fraction comparison is only valid when the wholes are equivalent.
	 * - Probability
	 * -- Impossible = 0, Certain = 1
	 * -- ExistingSoughtStates/TotalStates
	 * Recording Data
	 * - Probability Experiment
	 * - Bar Graphs
	 * - Tally Charts
	 * - Using an experiment chart to identify most probable event
	 * Length
	 * - Unit names
	 * - Tools for measurement
	 * - Estimate and measure
	 * Time
	 * - Unit names: Hour, Minute
	 * - Tools for measurement
	 * - Writing times (hh:mm)
	 * - Reading minutes from analog clock
	 * - Rounding minutes to nearest quarter hour.
	 * - Duration as time difference
	 * -- Hours only
	 * -- Hour to hour+minutes
	 * -- Hour+minutes to hour+minutes
	 * Volume
	 * - English Units
	 * - Metric Units
	 * - Tools for measurement
	 * Mass/Weight
	 * - English Units
	 * - Metric Units
	 * - Tools for measurement
	 * Unit Conversions (multiplication/division)
	 * - English Conversions
	 * - Metric Conversions
	 * - Length
	 * - Time
	 * - Volume
	 * - Mass/Weight
	 * Story Problems
	 * - Distinguishing superfluous/essential information.
	 * - Two-step story problems
	 * Approximate Solutions
	 * - Finding approximate answers (How to convince him to make approximations to simplify problem-solving rather than finding exact answers and identifying the correct approximation?)
	 * - Identifying when approximation if appropriate
	 */
}
