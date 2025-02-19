package app.Entities;

import javax.persistence.*;

@Entity

public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String url;

}
