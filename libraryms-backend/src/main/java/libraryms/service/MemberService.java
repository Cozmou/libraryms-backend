package libraryms.service;

import libraryms.model.Member;
import libraryms.model.Transaction;
import libraryms.repository.MemberRepository;
import libraryms.repository.TransactionRepository; // 1. Import your TransactionRepository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 2. Import Transactional

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository; // 3. Inject it safely here

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member save(Member member) {
        if (member.getMembershipDate() == null) {
            member.setMembershipDate(LocalDate.now());
        }
        return memberRepository.save(member);
    }

    public Optional<Member> update(Long id, Member updates) {
        return memberRepository.findById(id).map(existing -> {
            if (updates.getName()   != null) existing.setName(updates.getName());
            if (updates.getEmail()  != null) existing.setEmail(updates.getEmail());
            if (updates.getPhone()  != null) existing.setPhone(updates.getPhone());
            if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
            return memberRepository.save(existing);
        });
    }

    // 4. Wrap with @Transactional so the database processes both deletions seamlessly together
    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Business Rule Check: Safety guard so admins don't delete an active borrower who still owes books
        List<Transaction> activeLoans = transactionRepository.findAll().stream()
                .filter(t -> t.getMember() != null && t.getMember().getId().equals(id) && "borrowed".equals(t.getStatus()))
                .toList();

        if (!activeLoans.isEmpty()) {
            throw new RuntimeException("Cannot delete this member. They currently have active un-returned books out on loan!");
        }

        // 5. Query and safely wipe out any matching old returned transaction log histories first
        List<Transaction> attachedTransactions = transactionRepository.findAll().stream()
                .filter(t -> t.getMember() != null && t.getMember().getId().equals(id))
                .toList();

        if (!attachedTransactions.isEmpty()) {
            transactionRepository.deleteAll(attachedTransactions);
        }

        // 6. Now that the relational constraints are dropped, delete the member safely
        memberRepository.delete(member);
    }
}