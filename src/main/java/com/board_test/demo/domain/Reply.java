package com.board_test.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @JsonIgnore //json에서 제외시킴
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id")
    private Board board;

    @JsonIgnoreProperties({"parent", "child"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @JsonIgnoreProperties({"child"})
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Reply> child = new ArrayList<>();

    //==연관관계 메서드==//
    public void setBoard(Board board){
        if(this.board != null){
            this.board.getReplyList().remove(this);
        }
        this.board = board;
        board.getReplyList().add(this);
    }

    public void setParent(Reply parent) {
        if(this.parent != null) {
            this.parent.getChild().remove(this);
        }
        this.parent = parent;
        parent.getChild().add(this);
    }

    public void updateContent(String content){
        this.content = content;
    }

    @Builder
    public Reply(Long id, String content, Board board) {
        this.id = id;
        this.content = content;
        this.board = board;
    }
}
