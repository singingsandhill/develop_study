package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        //return memoRepository.findAll().stream().map(MemoResponseDto::new).toList();
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }

    public List<MemoResponseDto> getMemosByKeyword(String keyword) {
        return memoRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream().map(MemoResponseDto::new).toList();
    }

    @Transactional //변경감지를 사용하려면 영속성 컨텍스트가 필요하다 -> transactional 사용
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
//        Memo memo = memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 메모 존재하지 않음"));
        Memo memo = findMemo(id);
        // memo 내용 수정
        memo.update(requestDto);

        return id;
    }

    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 삭제
        memoRepository.delete(memo);

        return id;
    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}