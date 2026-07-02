package condition;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("String Precondition 테스트")
public class StringPreconditionTest {
    @Test
    @DisplayName("빈 문자열이 아닌 값을 요구할 수 있다")
    void require_non_blank() {
        assertThat(StringPrecondition.requireNonBlank("value", "value")).isEqualTo("value");

        assertThatThrownBy(() -> StringPrecondition.requireNonBlank(null, "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("value must not be null.");
        assertThatThrownBy(() -> StringPrecondition.requireNonBlank(" ", "value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value must not be blank.");
    }
}
