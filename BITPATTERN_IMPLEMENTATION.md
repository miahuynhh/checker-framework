# @BitPattern Implementation Summary

This document summarizes the implementation of the `@BitPattern` qualifier for issue #3221.

## Overview

The `@BitPattern` annotation represents values that are patterns of bits, not arithmetic quantities. Examples include:
- Return value of `Double.doubleToLongBits()`
- Values used as bitsets

## Type Hierarchy

The new type hierarchy is:

```
                    @UnknownSignedness (TOP)
                   /       |          \
            @Signed  @BitPattern  @Unsigned
                  \                /
                   @SignednessGlb
                          |
                   @SignedPositive
                          |
                  @SignednessBottom (BOTTOM)
```

## Operations Allowed/Forbidden

### ✅ Allowed on @BitPattern:
- **Bitwise operations**: `&`, `|`, `^`, `~`
- **All shifts**: `<<`, `>>`, `>>>`
- **Bitwise compound assignments**: `&=`, `|=`, `^=`
- **Shift compound assignments**: `<<=`, `>>=`, `>>>=`

### ❌ Forbidden on @BitPattern:
- **Arithmetic operations**: `+`, `-`, `*`, `/`, `%`
- **Comparisons**: `<`, `<=`, `>`, `>=`, `==`, `!=`
- **Arithmetic compound assignments**: `+=`, `-=`, `*=`, `/=`, `%=`

## Files Modified

### 1. **BitPattern.java** (NEW)
- Location: `checker-qual/src/main/java/org/checkerframework/checker/signedness/qual/BitPattern.java`
- New type qualifier annotation
- Subtype of `@UnknownSignedness`

### 2. **SignednessBottom.java** (MODIFIED)
- Location: `checker-qual/src/main/java/org/checkerframework/checker/signedness/qual/SignednessBottom.java`
- Updated `@SubtypeOf` to include `BitPattern.class`
- Now subtype of both `@SignedPositive` and `@BitPattern`

### 3. **SignednessAnnotatedTypeFactory.java** (MODIFIED)
- Location: `checker/src/main/java/org/checkerframework/checker/signedness/SignednessAnnotatedTypeFactory.java`
- Added import for `BitPattern`
- Added `BIT_PATTERN` annotation mirror constant

### 4. **SignednessVisitor.java** (MODIFIED)
- Location: `checker/src/main/java/org/checkerframework/checker/signedness/SignednessVisitor.java`
- Added import for `BitPattern`
- Added `hasBitPatternAnnotation()` helper method
- Updated `visitBinary()` to forbid arithmetic operations and comparisons on `@BitPattern`
- Updated `visitCompoundAssignment()` to forbid arithmetic compound assignments on `@BitPattern`
- Updated javadoc comments to document new rules

### 5. **messages.properties** (MODIFIED)
- Location: `checker/src/main/java/org/checkerframework/checker/signedness/messages.properties`
- Added error messages:
  - `operation.bitpatternlhs` - arithmetic operation with @BitPattern on left
  - `operation.bitpatternrhs` - arithmetic operation with @BitPattern on right
  - `comparison.bitpatternlhs` - comparison with @BitPattern on left
  - `comparison.bitpatternrhs` - comparison with @BitPattern on right
  - `compound.assignment.bitpattern.variable` - arithmetic compound assignment with @BitPattern variable
  - `compound.assignment.bitpattern.expression` - arithmetic compound assignment with @BitPattern expression

### 6. **signedness-checker.tex** (MODIFIED)
- Location: `docs/manual/signedness-checker.tex`
- Updated introduction to mention bit patterns
- Added documentation for `@BitPattern` annotation in the qualifiers section

### 7. **BitPatternOperations.java** (MODIFIED)
- Location: `checker/tests/signedness/BitPatternOperations.java`
- Removed `@skip-test` directive
- Updated test cases to match implemented error message keys
- Comprehensive tests for:
  - Allowed bitwise operations and shifts
  - Forbidden arithmetic operations
  - Forbidden comparisons
  - Forbidden arithmetic compound assignments
  - Allowed bitwise and shift compound assignments

## Testing

The test file `BitPatternOperations.java` contains comprehensive tests covering:

1. **Allowed uses**: Bitwise operations, shifts, and their compound assignment variants
2. **Forbidden arithmetic operations**: +, -, *, /, % with expected errors
3. **Forbidden comparisons**: <, <=, >, >=, ==, != with expected errors
4. **Forbidden compound assignments**: +=, -=, *=, /=, %= with expected errors

## Next Steps

To complete the implementation:

1. Build the project with Java 21+ (required by Checker Framework)
2. Run the Signedness Checker tests to verify the implementation
3. Check for any additional test files that need updates
4. Review issue #3210 and typetools/jdk#43 for JDK annotations that should use `@BitPattern`

## Related Issues

- Replaces: #3210
- Replaces: typetools/jdk#43
- Implements: #3221

