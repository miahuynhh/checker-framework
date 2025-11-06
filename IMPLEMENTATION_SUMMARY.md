# @BitPattern Implementation for Issue #3221

## Summary

Successfully implemented the `@BitPattern` type qualifier for the Signedness Checker, which represents values that are patterns of bits rather than arithmetic quantities (e.g., return values from `Double.doubleToLongBits()`, bitsets).

## Type Hierarchy

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

- `@BitPattern` is a subtype of `@UnknownSignedness`
- `@BitPattern` is a supertype of `@SignednessBottom`
- `@BitPattern` is **unrelated** to `@Signed` and `@Unsigned`

## Allowed and Forbidden Operations

### ✅ Allowed Operations
- **Bitwise operations**: `&`, `|`, `^`, `~`
- **All shifts**: `<<`, `>>`, `>>>`
- **Bitwise compound assignments**: `&=`, `|=`, `^=`
- **Shift compound assignments**: `<<=`, `>>=`, `>>>=`
- **Conversions**: to/from floating point values

### ❌ Forbidden Operations
- **Arithmetic operations**: `+`, `-`, `*`, `/`, `%`
- **Comparisons**: `<`, `<=`, `>`, `>=`, `==`, `!=`
- **Arithmetic compound assignments**: `+=`, `-=`, `*=`, `/=`, `%=`
- **Unary increment/decrement**: `++`, `--`
- **String concatenation**: `+` with strings

## Files Modified

### 1. New Annotation: `BitPattern.java`
**Location**: `checker-qual/src/main/java/org/checkerframework/checker/signedness/qual/BitPattern.java`

Created new type qualifier annotation with proper javadoc and `@SubtypeOf` declaration.

### 2. Updated: `SignednessBottom.java`
**Location**: `checker-qual/src/main/java/org/checkerframework/checker/signedness/qual/SignednessBottom.java`

**Change**: Updated `@SubtypeOf({SignedPositive.class, BitPattern.class})` to make bottom type a subtype of both.

### 3. Updated: `SignednessAnnotatedTypeFactory.java`
**Location**: `checker/src/main/java/org/checkerframework/checker/signedness/SignednessAnnotatedTypeFactory.java`

**Changes**:
- Added import for `BitPattern`
- Added `BIT_PATTERN` annotation mirror constant

### 4. Updated: `SignednessVisitor.java`
**Location**: `checker/src/main/java/org/checkerframework/checker/signedness/SignednessVisitor.java`

**Changes**:
- Added import for `BitPattern`
- Added `hasBitPatternAnnotation()` helper method
- Updated `visitBinary()` to check:
  - Arithmetic operations (/, %, +, -, *)
  - Comparisons (<, <=, >, >=, ==, !=)
  - String concatenation
- Updated `visitCompoundAssignment()` to check arithmetic compound assignments
- Added `visitUnary()` to check increment/decrement operations
- Updated javadoc comments

### 5. Updated: `messages.properties`
**Location**: `checker/src/main/java/org/checkerframework/checker/signedness/messages.properties`

**Added error messages**:
- `operation.bitpattern` - arithmetic operations not allowed
- `comparison.bitpattern` - comparisons not allowed
- `compound.assignment.bitpattern` - arithmetic compound assignments not allowed
- `unary.bitpattern` - increment/decrement not allowed
- `bitpattern.concat` - string concatenation not allowed

### 6. Updated: `signedness-checker.tex`
**Location**: `docs/manual/signedness-checker.tex`

**Changes**:
- Updated introduction to mention bit patterns
- Added `@BitPattern` documentation in the qualifiers section

### 7. Enabled Test: `BitPatternOperations.java`
**Location**: `checker/tests/signedness/BitPatternOperations.java`

**Change**: Removed `@skip-test` directive to enable the test.

The test file verifies:
- ✅ Bitwise operations and shifts work correctly
- ✅ Conversions to/from floating point work
- ❌ Arithmetic operations are forbidden
- ❌ Compound assignments are forbidden
- ❌ Unary increment/decrement are forbidden
- ❌ String concatenation is forbidden

## Implementation Details

### Error Checking Strategy

The implementation uses a uniform approach:
1. Check if operand(s) have `@BitPattern` annotation
2. Report error with appropriate message key
3. Allow the superclass visitor to continue normal processing

### Design Decisions

1. **Simple error messages**: Used single error keys (e.g., `operation.bitpattern`) rather than separate lhs/rhs variants for cleaner, more maintainable code.

2. **Comparison handling**: All comparisons (including `==` and `!=`) are forbidden on `@BitPattern` because these values represent bit patterns, not comparable quantities.

3. **Unary operations**: Only increment/decrement are forbidden. Bitwise NOT (`~`) is allowed as it's a bitwise operation.

4. **String concatenation**: Forbidden to prevent accidental use of bit patterns in user-facing strings.

## Testing

The test `BitPatternOperations.java` is now enabled and ready to run. It comprehensively tests:
- All allowed operations (no errors expected)
- All forbidden operations (errors expected with correct message keys)

## Next Steps

1. Build the project with Java 21+ (required by Checker Framework)
2. Run the Signedness Checker test suite:
   ```bash
   ./gradlew :checker:test --tests SignednessTest
   ```
3. Review issues #3210 and typetools/jdk#43 for JDK method return types that should be annotated with `@BitPattern`

## Related Issues

- Implements: #3221
- Replaces: #3210
- Replaces: typetools/jdk#43

