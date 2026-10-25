package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests the real member-card FXML on the JavaFX application thread.
 * Requires a graphical session and {@code HIVEMIND_UI_TESTS=true}.
 */
@EnabledIfEnvironmentVariable(named = "HIVEMIND_UI_TESTS", matches = "true")
public class PersonCardTest {

    @BeforeAll
    public static void initializeToolkit() throws Exception {
        CompletableFuture<Void> started = new CompletableFuture<>();
        Runnable initialize = () -> {
            Platform.setImplicitExit(false);
            started.complete(null);
        };
        try {
            Platform.startup(initialize);
        } catch (IllegalStateException e) {
            // Another UI test may already have started the toolkit in this test process.
            Platform.runLater(initialize);
        }
        started.get(10, TimeUnit.SECONDS);
    }

    @Test
    public void constructor_allFieldsPresent_displaysDetails() throws Exception {
        runOnFxThread(() -> {
            Person person = new PersonBuilder().withName("Alicia Tan").withPhone("90000002")
                    .withEmail("alicia@example.com").withStatus("active-alumnus").withMemberType("teacher")
                    .withRole("Section Leader").withInstruments("Violin:1", "piano:5")
                    .withGroups("zeta", "Alpha").build();
            PersonCard card = new PersonCard(person, 3);

            assertEquals("3. ", label(card, "id").getText());
            assertEquals("Alicia Tan", label(card, "name").getText());
            assertEquals("90000002", label(card, "phone").getText());
            assertEquals("alicia@example.com", label(card, "email").getText());
            assertEquals("active-alumnus", label(card, "membershipStatus").getText());
            assertEquals("teacher", label(card, "memberType").getText());
            assertEquals("Role: Section Leader", label(card, "role").getText());
            assertTrue(label(card, "role").isVisible());
            assertTrue(label(card, "role").isManaged());

            FlowPane instruments = (FlowPane) card.getRoot().lookup("#instruments");
            assertNotNull(instruments);
            List<String> instrumentLabels = instruments.getChildren().stream()
                    .map(node -> ((Label) node).getText()).toList();
            assertEquals(List.of("piano (5 yrs)", "Violin (1 yr)"), instrumentLabels);
            assertEquals(List.of("Alpha", "zeta"), groupNames(card));
            assertTrue(card.getRoot().lookup("#groups").isVisible());
            assertTrue(card.getRoot().lookup("#groups").isManaged());
        });
    }

    @Test
    public void constructor_optionalFieldsAbsent_hidesRoleAndHasNoInstruments() throws Exception {
        runOnFxThread(() -> {
            PersonCard card = new PersonCard(new PersonBuilder().build(), 1);

            assertEquals("", label(card, "role").getText());
            assertFalse(label(card, "role").isVisible());
            assertFalse(label(card, "role").isManaged());
            FlowPane instruments = (FlowPane) card.getRoot().lookup("#instruments");
            assertNotNull(instruments);
            assertTrue(instruments.getChildren().isEmpty());
            assertTrue(groupNames(card).isEmpty());
            assertFalse(card.getRoot().lookup("#groups").isVisible());
            assertFalse(card.getRoot().lookup("#groups").isManaged());
        });
    }

    @Test
    public void constructor_groupNameContainingComma_distinguishesSeparateGroups() throws Exception {
        runOnFxThread(() -> {
            PersonCard oneGroup = new PersonCard(new PersonBuilder().withGroups("Dance, Music").build(), 1);
            PersonCard twoGroups = new PersonCard(new PersonBuilder().withGroups("Dance", "Music").build(), 2);

            assertEquals(List.of("Dance, Music"), groupNames(oneGroup));
            assertEquals(List.of("Dance", "Music"), groupNames(twoGroups));
        });
    }

    @Test
    public void constructor_membershipCombinations_displaysCorrectValues() throws Exception {
        runOnFxThread(() -> {
            for (String status : List.of("member", "active-alumnus", "inactive-alumnus")) {
                for (String memberType : List.of("student", "teacher")) {
                    Person person = new PersonBuilder().withStatus(status).withMemberType(memberType).build();
                    PersonCard card = new PersonCard(person, 1);

                    assertEquals(status, label(card, "membershipStatus").getText());
                    assertEquals(memberType, label(card, "memberType").getText());
                }
            }
        });
    }

    private static List<String> groupNames(PersonCard card) {
        FlowPane labels = (FlowPane) card.getRoot().lookup("#groupLabels");
        assertNotNull(labels);
        return labels.getChildren().stream().map(node -> ((Label) node).getText()).toList();
    }

    private static Label label(PersonCard card, String id) {
        Label label = (Label) card.getRoot().lookup("#" + id);
        assertNotNull(label, "Missing FXML label: " + id);
        return label;
    }

    /**
     * Runs assertions on the JavaFX thread and propagates failures to JUnit with a bounded wait.
     */
    private static void runOnFxThread(Runnable assertions) throws Exception {
        CompletableFuture<Void> result = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                assertions.run();
                result.complete(null);
            } catch (Throwable error) {
                result.completeExceptionally(error);
            }
        });
        result.get(10, TimeUnit.SECONDS);
    }
}
