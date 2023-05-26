package server.book_library.domain.library.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.loan.entity.Loan;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.inventory.repository.LibraryInventoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryInventoryService {
    private final LibraryInventoryRepository libraryInventoryRepository;

    //캐시를 저장하는것 = 공통기능으로 묶은
    @CachePut(value = "libraryInventory", key = "#result.id", unless = "#result == null", cacheManager = "cacheManagerTest")
    public LibraryInventory createLibraryInventory(LibraryInventory libraryInventory) {
        extracted(libraryInventory);
        isDeletedBook(libraryInventory);

        return libraryInventoryRepository.save(libraryInventory);
    }

    private void extracted(LibraryInventory libraryInventory) {
        List<LibraryInventory> libraryInventories = findAll();
        boolean isDuplicate = libraryInventories.stream()
                .anyMatch(libraryInventory1 ->
                        libraryInventory1.getBook().getId().equals(libraryInventory.getBook().getId()) &&
                                libraryInventory1.getLibrary().getId().equals(libraryInventory.getLibrary().getId()) &&
                                !libraryInventory1.isDeleted()
                );

        if (isDuplicate) {
            throw new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_ALREADY_EXISTS);
        }
    }

    private void isDeletedBook(LibraryInventory libraryInventory){
        if(libraryInventory.getBook().isDeleted()) {
            throw new BusinessLogicException(ExceptionCode.BOOK_HAS_DELETE);
        }
    }

    public LibraryInventory updateLibraryInventory(LibraryInventory libraryInventory) {
        LibraryInventory findLibraryInventory = findById(libraryInventory.getId());
        int loanQuantity = findLibraryInventory.getLoanQuantity();
        int totalQuantity = findLibraryInventory.getTotalQuantity();

        if(loanQuantity > totalQuantity) {
            throw new BusinessLogicException(ExceptionCode.QUANTITY_UPDATE_IMPOSSIBLE);
        }
        else {
            Optional.of(libraryInventory.getTotalQuantity()).ifPresent(findLibraryInventory::setTotalQuantity);
            if( loanQuantity == totalQuantity) {
                findLibraryInventory.setLoanStatus(LibraryInventory.LoanStatus.모두대여중);
            }
            return libraryInventoryRepository.save(findLibraryInventory);
        }
    }

    public List<LibraryInventory> findAll() {
        return libraryInventoryRepository.findAll();
    }

    public LibraryInventory deleteLibraryInventory(LibraryInventory libraryInventory) {
        libraryInventory.setDeleted(true);
        return libraryInventoryRepository.save(libraryInventory);
    }

    public void plusLoanQuantity(LibraryInventory libraryInventory) {
        libraryInventory.setLoanQuantity(libraryInventory.getLoanQuantity() + 1);
        setLoanStatus(libraryInventory);
    }

    public void minusLoanQuantity(LibraryInventory libraryInventory) {
        libraryInventory.setLoanQuantity(libraryInventory.getLoanQuantity() - 1);
        setLoanStatus(libraryInventory);
    }

    private static void setLoanStatus(LibraryInventory libraryInventory) {
        if(libraryInventory.getLoanQuantity() == libraryInventory.getTotalQuantity()) {
            libraryInventory.setLoanStatus(LibraryInventory.LoanStatus.모두대여중);
        }
        else if (libraryInventory.getLoanQuantity() < libraryInventory.getTotalQuantity()) {
            libraryInventory.setLoanStatus(LibraryInventory.LoanStatus.대여가능);
        }
    }

    public LibraryInventory findById(long id) {
        Optional<LibraryInventory> optionalLibraryInventory = libraryInventoryRepository.findById(id);
        LibraryInventory libraryInventory = optionalLibraryInventory.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_OUT_OF_STOCK));
        List<Loan> loans = new ArrayList<>(libraryInventory.getLoans());

        for(Loan loan: loans) {
            loan.setMember(loan.getMember());
        }
        libraryInventory.setLoans(loans);
        if(libraryInventory.isDeleted()) {
            throw new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_IS_DELETED);
        }
        else return libraryInventory;
    }

    public void validLoanStatus(LibraryInventory libraryInventory) {
        LibraryInventory.LoanStatus loanStatus = libraryInventory.getLoanStatus();
        if(loanStatus.equals(LibraryInventory.LoanStatus.모두대여중)) {
            throw new BusinessLogicException(ExceptionCode.LOAN_NOT_ALLOW);
        }
    }
}
