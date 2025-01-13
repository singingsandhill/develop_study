package com.sparta.memo.dto;

import com.sparta.memo.entity.Memo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class MemoRequestDto {
    private String username;
    private String contents;

    public MemoRequestDto(Memo memo) {
        this.username = memo.getUsername();
        this.contents = memo.getContents();
    }
}