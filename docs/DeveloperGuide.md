---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in `docs/diagrams`. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The alternative, arguably more object-oriented, design below keeps a unique list of tags in `AddressBook`, and each `Person` references tags from that list. This lets `AddressBook` maintain one `Tag` object per unique tag instead of each `Person` holding its own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Partial-name search

The `find` command matches any part of a person's name, ignoring case. For example, `find ali` matches `Alice Tan`. Multiple whitespace-separated keywords use OR: a person is included when at least one keyword matches. Only the name is searched.

`FindCommandParser` rejects empty arguments and splits valid arguments into keywords. It constructs a `FindCommand` containing a `NameContainsKeywordsPredicate`. When executed, the command passes the predicate to `ModelManager.updateFilteredPersonList`, which updates the predicate of the JavaFX `FilteredList`. The UI observes this list and displays the matching persons with indices starting at 1; the underlying address book remains unchanged.

`NameContainsKeywordsPredicate` uses `StringUtil.containsSubstringIgnoreCase` for each keyword. The helper trims the keyword, rejects null, empty or multiple-word inputs, and normalises both strings with `Locale.ROOT` before checking for a substring. The original `containsWordIgnoreCase` helper retains its whole-word behaviour for other callers.

Tests in `StringUtilTest` cover substring positions, case differences, invalid inputs and non-matches. `NameContainsKeywordsPredicateTest` verifies name-only matching and OR semantics. `FindCommandTest` checks the resulting persons and feedback count, while `FindCommandParserTest` covers empty arguments and extra whitespace.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a coordinator or executive committee member of a Music CCA
* manages the records of about 50–200 members and alumni of that CCA, including both students and teachers
* deals with membership that changes frequently (e.g., new intakes each semester, members graduating to become alumni)
* often needs to find suitable players for a piece or activity based on the instruments they play and how long they have played them
* prefers desktop apps over other types of applications
* can type fast, and prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: HiveMind keeps a single, searchable directory of the contact details, membership status, roles and musical experience of a Music CCA's members and alumni. It lets the coordinator shortlist players for a piece or activity (e.g., "current members who have played trumpet for at least 3 years") in a single command, faster than scanning a spreadsheet or a typical mouse-driven GUI app.

**Out of scope**: HiveMind manages the directory of one CCA only. It does not handle event registration, attendance taking, or communication and mass messaging with members.


### User stories

Priorities: High (must have), Medium (nice to have), Low (unlikely to have)

| Story ID | Scenario | As a | I can | So that | Urgency |
| --- | --- | --- | --- | --- | --- |
| US01 | First use | first-time coordinator | explore representative sample records with guidance on key tasks | I can learn how HiveMind works without affecting real member information | Low |
| US02 | Second use | society coordinator | add a person with their name and contact details | I can maintain one reliable directory for the society | High |
| US03 | Second use | society coordinator | view everyone recorded in the directory | I can review all people affiliated with the society | High |
| US04 | Second use | society coordinator | view all recorded details for one person | I can retrieve their contact, membership, and musical information | High |
| US05 | Second use | society coordinator | update any recorded detail for a person | the directory remains accurate as information changes | High |
| US06 | Second use | society coordinator adopting an existing directory | import existing member information in bulk | I can transfer existing records without recreating every person manually | Medium |
| US07 | 10th use | society coordinator | record whether a person is a current member or an alumnus | I can distinguish present membership from past affiliation | High |
| US08 | 10th use | society coordinator | record whether a person is actively participating | I can identify current participants, including active alumni | High |
| US09 | 10th use | society coordinator | record whether a person is a student or teacher | I can distinguish players from potential instructors | Medium |
| US10 | 10th use | society coordinator | record a person's society role | I can identify people with particular responsibilities | Medium |
| US11 | 10th use | society coordinator | record the participation groups a person belongs to | I can identify the groups in which they are involved | Medium |
| US12 | 10th use | society coordinator | record all instruments a person plays | I can identify candidates for each musical part | High |
| US13 | 10th use | society coordinator | record a person's musical experience | I can assess their relevant musical background | Medium |
| US14 | 10th use | society coordinator | record a person's competency for each instrument | I can judge whether they can handle demanding parts | Medium |
| US15 | 10th use | society coordinator looking for a known person | search for them by name | I can retrieve their information quickly | High |
| US16 | 10th use | society coordinator | filter people by membership relationship and participation status | I can find current members, alumni, or active alumni without confusing the groups | High |
| US17 | 10th use | society coordinator seeking musical advice | filter people by student or teacher status | I can identify an appropriate instructor to approach | Medium |
| US18 | 10th use | society coordinator | filter people by society role | I can find people with a required responsibility | Medium |
| US19 | 10th use | society coordinator organising a group | filter people by participation group | I can find everyone involved in the relevant group | Medium |
| US20 | 10th use | society coordinator staffing a musical part | filter people by instrument | I can find candidates who play the required instrument | High |
| US21 | 10th use | society coordinator planning a piece | filter people by musical experience | I can find candidates with sufficient relevant background | Medium |
| US22 | 10th use | society coordinator planning a difficult piece | filter people by instrument competency | I can find candidates suited to the piece's difficulty | Medium |
| US23 | 10th use | society coordinator selecting people for an activity | apply several directory criteria together | I can produce a shortlist satisfying all relevant requirements | High |
| US24 | 100th use | long-term society coordinator | remove an obsolete or incorrectly added record | stale or incorrect information does not clutter or mislead me | High |
| US25 | 100th use | society coordinator who has identified a relevant group | export chosen details for the people in my current results | I can use a focused roster outside HiveMind without unrelated information | Medium |
| US26 | 100th use | society coordinator who made one or more mistakes | undo recent changes | I can recover from accidental edits or deletions without re-entering information | Medium |
| US27 | 100th use | society coordinator who undid a correct change | redo the undone change | I can restore it without entering the information again | Low |
| US28 | 100th use | society coordinator comparing members | sort directory results by a chosen recorded attribute | I can compare or browse people in an order useful to me | Low |
| US29 | After a prolonged break | returning society coordinator | access my previously saved records | I can continue managing the society without rebuilding the directory | High |
| US30 | After a prolonged break | returning society coordinator | access concise guidance for HiveMind's main functions | I can refresh my memory and resume work confidently | High |

### Use cases

**System:** HiveMind

**Use case:** UC01 - Add a member

**Actor:** Society coordinator

**MSS:**

1.  Society coordinator requests to add a member.
2.  HiveMind requests the member's name and contact details, and any membership or musical details to record.
3.  Society coordinator supplies the requested details.
4.  HiveMind adds the member to the directory and shows the saved record.

    Use case ends.

**Extensions:**

* 3a. HiveMind detects that a required detail is missing or a supplied detail is invalid.

    * 3a1. HiveMind shows an error message describing the problem.
    * 3a2. Society coordinator supplies corrected details.

      Steps 3a1-3a2 are repeated until the supplied details are valid.
      Use case resumes at step 4.

* 3b. HiveMind detects that the supplied name or phone number matches an existing record.

    * 3b1. HiveMind warns the society coordinator about each possible duplicate.

      Use case resumes at step 4.

* 4a. HiveMind cannot save the updated directory.

    * 4a1. HiveMind shows an error message informing the society coordinator that the new record was not saved.

      Use case ends.

**System:** HiveMind

**Use case:** UC02 - Update a member's details

**Actor:** Society coordinator

**MSS:**

1.  Society coordinator requests to update a member's recorded details.
2.  HiveMind requests the member to update and the details to change.
3.  Society coordinator identifies the member and supplies the new details, such as contact, membership, or musical information.
4.  HiveMind updates the supplied details, leaves other details unchanged, and shows the updated record.

    Use case ends.

**Extensions:**

* 3a. HiveMind detects that the selection is invalid or the member does not exist in the displayed list.

    * 3a1. HiveMind shows an error message and requests a valid selection.
    * 3a2. Society coordinator identifies a member in the displayed list.

      Steps 3a1-3a2 are repeated until the selection is valid.
      Use case resumes at step 4.

* 3b. HiveMind detects that no updated detail was supplied or a supplied detail is invalid.

    * 3b1. HiveMind shows an error message describing the problem.
    * 3b2. Society coordinator supplies corrected details.

      Steps 3b1-3b2 are repeated until the supplied details are valid.
      Use case resumes at step 4.

* 4a. HiveMind cannot save the updated directory.

    * 4a1. HiveMind shows an error message informing the society coordinator that the change was not saved.

      Use case ends.

**System:** HiveMind

**Use case:** UC03 - Delete a member

**Actor:** Society coordinator

**MSS:**

1.  Society coordinator requests to remove an obsolete or incorrectly added member record.
2.  HiveMind requests the member to remove.
3.  Society coordinator identifies the member in the displayed list.
4.  HiveMind removes the member's record from the directory and confirms which member was removed.

    Use case ends.

**Extensions:**

* 3a. HiveMind detects that the selection is invalid or the member does not exist in the displayed list.

    * 3a1. HiveMind shows an error message and requests a valid selection.
    * 3a2. Society coordinator identifies a member in the displayed list.

      Steps 3a1-3a2 are repeated until the selection is valid.
      Use case resumes at step 4.

* 4a. HiveMind cannot save the updated directory.

    * 4a1. HiveMind shows an error message informing the society coordinator that the deletion was not saved.

      Use case ends.

**System:** HiveMind

**Use case:** UC04 - Shortlist suitable players

**Actor:** Society coordinator

**MSS:**

1.  Society coordinator requests to shortlist suitable players.
2.  HiveMind requests the filtering criteria.
3.  Society coordinator supplies one or more criteria, such as status, instrument, or musical experience.
4.  HiveMind shows the members who satisfy the criteria.

    Use case ends.

**Extensions:**

* 3a. HiveMind detects that no criterion was supplied or a supplied criterion is invalid.

    * 3a1. HiveMind shows an error message describing the problem.
    * 3a2. Society coordinator supplies corrected criteria.

      Steps 3a1-3a2 are repeated until the supplied criteria are valid.
      Use case resumes at step 4.

* 4a. No member satisfies the criteria.

    * 4a1. HiveMind informs the society coordinator that no members were found.

      Use case ends.

### Non-Functional Requirements

1.  HiveMind should run on any _mainstream OS_ that has Java `25` or above installed.
2.  With a directory of up to 1,000 members, HiveMind should complete `list`, `find`, and `filter` operations within 2 seconds on a computer with a 2 GHz quad-core processor and 8 GB of RAM.
3.  A coordinator should be able to `add`, `edit`, `delete`, `list`, `find`, and `filter` operations using only the keyboard.
4.  HiveMind should display success or error feedback for each submitted command.
5.  Invalid syntax, field values, or displayed indices should produce an error explaining the rejection without changing member records.
6.  With a directories of up to 1,000 members, HiveMind should automatically save each change before reporting success, given that the local disk has sufficient free space.

### Glossary

* **Active alumnus**: An alumnus who still plays with the CCA (e.g., in alumni pieces or events).
* **Alumnus**: A former member of the CCA. An alumnus is either an _active alumnus_ or an _inactive alumnus_.
* **CCA**: Co-Curricular Activity. A student society, such as a school band or orchestra.
* **Coordinator**: The user of HiveMind, i.e., the person in the CCA who is responsible for maintaining its member records and organising players for pieces and activities.
* **Directory**: The full set of member records stored in HiveMind.
* **Displayed index**: The number shown next to a member in the list currently displayed. It changes after a `find`, `filter` or `list`, and is used to refer to a member in commands such as `delete`.
* **Filter**: To display only the members who meet all the given criteria (e.g., status, instrument, minimum years played). Unlike _find_, which searches by name only.
* **Inactive alumnus**: An alumnus who no longer plays with the CCA, but whose record is kept (e.g., for contacting them about alumni events).
* **Instrument experience**: The whole number of years a member has played a particular instrument. It is recorded separately for each instrument, and is used as the measure of a member's competency on that instrument.
* **Mainstream OS**: Windows, Linux, Unix, or macOS.
* **Member**: A person who currently belongs to the CCA. In HiveMind, _member_ is also used loosely to mean any person in the directory, including alumni and teachers.
* **Participation group**: A group of members formed for a particular piece, activity or training programme (e.g., a group of new members under training).
* **Possible duplicate**: A person being added whose name or phone number matches that of an existing record. HiveMind warns the user but still adds the person, since two different people can share a name or a phone number.
* **Prefix**: The short label before a parameter in a command (e.g., `n/` in `n/Tan Wei Ming`) that tells HiveMind which field the value belongs to.
* **Role**: A position a person holds in the CCA (e.g., Section Leader, Conductor). Roles are free text, since each CCA names its roles differently.
* **Status**: Whether a person is a _member_, an _active alumnus_ or an _inactive alumnus_ of the CCA.
* **Type**: Whether a person is a _student_ or a _teacher_ (e.g., an instructor or teacher-in-charge) of the CCA.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command, with multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: The first contact is deleted from the list. The status message shows the deleted contact's details.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. The status message shows error details.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases …​ }_
