package com.nuce.duantp.sunshine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_table")
public class tbl_Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tablename")
    private String tablename;

    @Column(name = "seat") //số chỗ ngồi
    private int seat;

    @Column(name = "stillEmpty") //số lượng con trống
    private int stillEmpty;

    public tbl_Table(String tablename, int seat) {
        this.tablename = tablename;
        this.seat = seat;
    }
}
