package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class Books  implements Serializable {
    private static final long serialVersionUID=8480595839967663206L;

    @Id
    private String id;
    @NotEmpty
    @Size(min =3,max=20,message = "Name length invalid")
    private String name;
    @NotNull(message = "ISBN can not be empty")
    @Size(min =10,max=13,message = "ISBN length invalid")
    private String isbn;
    private String plot;
    @NotEmpty
    @Size(min =3,max=10,message = "Author length invalid")
    private String author;
    @NotEmpty
    @Size(min =3,max=10,message = "Genre length invalid")
    private String genre;
    private boolean isAvailable;

    //String id, String isbn, String name, String plot, String author,
    //String genre, boolean isAvailable.

    public Books() {
    }
}
