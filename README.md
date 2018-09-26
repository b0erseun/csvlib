# csvlib
Java library for reading csv files into POJO's

The library makes use of reflection to initialize objects with the values in the csv file.  Fields, constructor parameters or setters can 
be annotated with `@CsvColumn(<columnName>)` to indicate which column of the csv file should be used.  
The CsvReader is initialized with a file path or an inputstream.  You can also specify if the csv file contains a header row. 
If no header row is specified the column indexes can be used.  The column indexes are 0 based.
Example:
```java
   @CsvColumn(columnIndex = <index>)
   private String name;
   
```



## annotating fields
Fields that are annotated cannot be declared final, but they can be declared private.  If your fields have to be final, 
use the annotation inside the constructor.

```java
public class Person {
   
   @CsvColumn("firstname")
   private String firstName;
   
   @CsvColumn("surname")
   private String surname;
   ...
   ...
```

## annotating setters
Methods that return void and take 1 parameter can be annotated.

```java
  @CsvColumn("name")
  public void setName(String name) {
  ...
```

## annotating constructors
When using the annotation inside a constructor, all parameters have to be annotated before that constructor will be used.

```java
public class Person {
   private final String name;
   private final String surname;
   
   public Person(@CsvColumn("name") String name, @CsvColumn("surname") String surname) {
      this.name = name;
      this.surname = surname;
   }
 }
 ```

## custom deserializers
You can create your own deserializers if you class has fields of types that are not handled by default.  Your deserializer needs to implement the CsvDeserializer interface.
When values need to be deserialized, deserializers are tested one by one until a deserializer is found that can desrialize the specified type.  Deserializers must be able to handle null values for `serializedData`

an example implementation of the canDesrialize method would be `   return LocalDate.class == clazz; `

```java
public interface CsvDeserializer {

    public Object deserialize(Class<?> clazz, String serializedData);

    boolean canDeserialize(Class<?> clazz);

}
```

Deserializers for the following types are added by default.
* All primitives  
* String (obviously)
* LocalDateTime



## Example usage:
```java
 CsvReader<PersonM> peopleReader = new CsvReaderBuilder<>(PersonM.class)
         .withFilePath("c:/csvlibtest/people.csv")
         .withCustomDeserializer(new LocalDateDeserializer())
         .withHasHeaderRow()
         .build();


 try {
     List<PersonM> people = peopleReader.readCsvFile();
```
And the PersonM class might look like this:

```java
public class PersonM {
    private String name;

    private String surname;

    private String idNUmber;

    LocalDate dateOfBirth;

    public PersonM() {
    }

    public String getName() {
        return name;
    }

    @CsvColumn("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    @CsvColumn("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNUmber() {
        return idNUmber;
    }

    @CsvColumn("idnumber")
    public void setIdNUmber(String idNUmber) {
        this.idNUmber = idNUmber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @CsvColumn("dob")
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "PersonM{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", idNUmber='" + idNUmber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
```

csv file:
```
name,surname,idnumber,dob
"sam","Cronje",7010219999888,"1970-10-21"
```
