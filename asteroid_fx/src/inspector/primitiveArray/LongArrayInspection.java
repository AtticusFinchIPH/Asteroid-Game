package inspector.primitiveArray;

import inspector.Inspection;
import inspector.PrimitiveInspection;

import java.util.function.Supplier;

public class LongArrayInspection extends PrimitiveArrayInspection {

  private long[] array;
  private final Supplier<long[]> getter;

  public LongArrayInspection(Supplier<long[]> getter, String name) {
    super(name);
    this.getter = getter;
    initialize();
  }

  @Override
  protected boolean arrayIsNull() {
    return array == null;
  }

  protected void resetArray() {
    array = getter.get();
  }

  protected boolean arrayHasChanged() {
    return getter.get() != array;
  }

  @Override
  protected Inspection makeInspection(int i, String indexName) {
    return new PrimitiveInspection(() -> array[i], indexName);
  }

  @Override
  protected int getLength() {
    return  array == null ? 0 : array.length;
  }

}

