package hw4;

import java.util.Iterator;
import java.util.Stack;

/**
 * <b>RatPolyStack</B> is a mutable finite sequence of RatPoly objects.
 * <p>
 * Each RatPolyStack can be described by [p1, p2, ... ], where [] is an empty
 * stack, [p1] is a one element stack containing the Poly 'p1', and so on.
 * RatPolyStacks can also be described constructively, with the append
 * operation, ':'. such that [p1]:S is the result of putting p1 at the front of
 * the RatPolyStack S.
 * <p>
 * A finite sequence has an associated size, corresponding to the number of
 * elements in the sequence. Thus the size of [] is 0, the size of [p1] is 1,
 * the size of [p1, p1] is 2, and so on.
 */
public final class RatPolyStack implements Iterable<RatPoly> {

  private final Stack<RatPoly> polys;

  // Abstraction Function:
  // Each element of a RatPolyStack, s, is mapped to the
  // corresponding element of polys.
  //
  // RepInvariant:
  // polys != null &&
  // forall i such that (0 <= i < polys.size(), polys.get(i) != null

  /**
   * @spec.effects Constructs a new RatPolyStack, [].
   */
  public RatPolyStack() {
    polys = new Stack<RatPoly>();
    checkRep();
  }

  /**
   * Returns the number of RayPolys in this RatPolyStack.
   *
   * @return the size of this sequence.
   */
  public int size() {
	  return polys.size();
  }

  /**
   * Pushes a RatPoly onto the top of this.
   *
   * @param p The RatPoly to push onto this stack.
   * @spec.requires p != null
   * @spec.modifies this
   * @spec.effects this_post = [p]:this
   */
  public void push(RatPoly p) {
	  polys.push(p);
  }

  /**
   * Removes and returns the top RatPoly.
   *
   * @spec.requires this.size() &gt; 0
   * @spec.modifies this
   * @spec.effects If this = [p]:S then this_post = S
   * @return p where this = [p]:S
   */
  public RatPoly pop() {
	  return polys.pop();
  }

  /**
   * Duplicates the top RatPoly on this.
   *
   * @spec.requires this.size() &gt; 0
   * @spec.modifies this
   * @spec.effects If this = [p]:S then this_post = [p, p]:S
   */
  public void dup() {
	  polys.push(polys.peek());
  }

  /**
   * Swaps the top two elements of this.
   *
   * @spec.requires this.size() &gt;= 2
   * @spec.modifies this
   * @spec.effects If this = [p1, p2]:S then this_post = [p2, p1]:S
   */
  public void swap() {
	  RatPoly temp1 = polys.pop();
	  RatPoly temp2 = polys.pop();
	  
	  polys.push(temp1);
	  polys.push(temp2);
  }

  /**
   * Clears the stack.
   *
   * @spec.modifies this
   * @spec.effects this_post = []
   */
  public void clear() {
	  polys.clear();
  }

  /**
   * Returns the RatPoly that is 'index' elements from the top of the stack.
   *
   * @param index The index of the RatPoly to be retrieved.
   * @spec.requires index &gt;= 0 &amp; &amp; index &lt; this.size()
   * @return If this = S:[p]:T where S.size() = index, then returns p.
   */
  public RatPoly getNthFromTop(int index) {
	  if (polys.size() == index) {
		  return polys.peek();
	  }
	  
	  Stack<RatPoly> temp = new Stack<RatPoly>();
	  int i = 0;
	  
	  // {{Inv: i < index and index has not been reached}}
	  while (i != index) {
		  temp.push(polys.pop());
		  i++;
	  }
	  
	  RatPoly tempPoly = polys.peek();
	  
	  // {{Inv: temp is not empty and polys does not have all of its elements}}
	  while (!temp.isEmpty()) {
		  polys.push(temp.pop());
	  }
	  
	  return tempPoly;
  }

  /**
   * Pops two elements off of the stack, adds them, and places the result on
   * top of the stack.
   *
   * @spec.requires this.size() &gt;= 2
   * @spec.modifies this
   * @spec.effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p1 + p2
   */
  public void add() {
	  RatPoly temp1 = polys.pop();
	  RatPoly temp2 = polys.pop();
	  
	  polys.push(temp1.add(temp2));
  }

  /**
   * Subtracts the top poly from the next from top poly, pops both off the
   * stack, and places the result on top of the stack.
   *
   * @spec.requires this.size() &gt;= 2
   * @spec.modifies this
   * @spec.effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p2 - p1
   */
  public void sub() {
	  RatPoly temp1 = polys.pop();
	  RatPoly temp2 = polys.pop();
	  
	  polys.push(temp2.sub(temp1));
  }

  /**
   * Pops two elements off of the stack, multiplies them, and places the
   * result on top of the stack.
   *
   * @spec.requires this.size() &gt;= 2
   * @spec.modifies this
   * @spec.effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p1 * p2
   */
  public void mul() {
	  RatPoly temp1 = polys.pop();
	  RatPoly temp2 = polys.pop();
	  
	  polys.push(temp1.mul(temp2));
  }

  /**
   * Divides the next from top poly by the top poly, pops both off the stack,
   * and places the result on top of the stack.
   *
   * @spec.requires this.size() &gt;= 2
   * @spec.modifies this
   * @spec.effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p2 / p1
   */
  public void div() {
	  RatPoly temp1 = polys.pop();
	  RatPoly temp2 = polys.pop();
	  
	  polys.push(temp2.div(temp1));
  }

  /**
   * Pops the top element off of the stack, differentiates it, and places the
   * result on top of the stack.
   *
   * @spec.requires this.size() &gt;= 1
   * @spec.modifies this
   * @spec.effects If this = [p1]:S then this_post = [p2]:S where p2 = derivative
   *          of p1
   */
  public void differentiate() {
	  RatPoly temp = polys.pop();
	  
	  polys.push(temp.differentiate());
  }

  /**
   * Pops the top element off of the stack, integrates it, and places the
   * result on top of the stack.
   *
   * @spec.requires this.size() &gt;= 1
   * @spec.modifies this
   * @spec.effects If this = [p1]:S then this_post = [p2]:S where p2 = indefinite
   *          integral of p1 with integration constant 0
   */
  public void integrate() {
	  RatPoly temp = polys.pop();
	  
	  polys.push(temp.antiDifferentiate(RatNum.ZERO));
  }

  /**
   * Returns an iterator of the elements contained in the stack.
   *
   * @return an iterator of the elements contained in the stack in order from
   *         the bottom of the stack to the top of the stack.
   */
  @Override
  public Iterator<RatPoly> iterator() {
    return polys.iterator();
  }

  /**
   * Checks that the representation invariant holds (if any).
   */
  private void checkRep() {
    assert (polys != null) : "polys should never be null.";
    
    for (RatPoly p : polys) {
        assert (p != null) : "polys should never contain a null element.";
    }
  }
}
