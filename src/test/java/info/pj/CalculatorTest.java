package info.pj;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatorTest {


    private Individual a;
    private Individual b;
    private Individual e;
    private Individual c;
    private Individual d;
    private Individual f;
    private Individual g;
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        a = Individual.founder("A", 1970);
        b = Individual.founder("B", 1971);
        e = Individual.founder("E", 1972);

        c = Individual.descendant("C", 1981, a, b);
        d = Individual.descendant("D", 1983, a, b);

        f = Individual.descendant("F", 1991, d, e);
        g = Individual.descendant("G", 1992, d, e);

        calculator = new Calculator(Set.of(a, b, c, d, e, f, g));

    }

    @Test
    void unrelated() {
        assertThat(Calculator.relatedness(a, b)).isZero();
        assertThat(Calculator.relatedness(b, a)).isZero();
    }

    @Test
    void parent() {
        assertThat(Calculator.relatedness(c, a)).isEqualTo(0.5);
        assertThat(Calculator.relatedness(a, c)).isEqualTo(0.5);
    }

    @Test
    void sibling() {
        assertThat(Calculator.relatedness(c, d)).isEqualTo(0.5);
        assertThat(Calculator.relatedness(d, c)).isEqualTo(0.5);
    }

    @Test
    void gen2parent() {
        assertThat(Calculator.relatedness(a, f)).isEqualTo(0.25);
        assertThat(Calculator.relatedness(f, a)).isEqualTo(0.25);
    }

    @Test
    void founderContributions() {
        assertThat(calculator.founderEquivalents()).isEqualTo(2.91d, Offset.offset(0.0051));
    }

    @Test
    void founderGenomeContributions() {
        assertThat(calculator.founderGenomeEquivalents()).isEqualTo(2.18d, Offset.offset(0.0051));
    }


}