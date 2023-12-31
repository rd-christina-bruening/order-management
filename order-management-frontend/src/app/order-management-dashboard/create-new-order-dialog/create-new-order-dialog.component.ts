import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {OrderData} from '../order-management-dashboard.component';
import {NgxFileDropEntry} from 'ngx-file-drop';
import {NotificationService} from '../../notification/notification.service';
import {OrderInputDto, OrderManagementService} from '../../order-management.service';

export type FileUploadItem = {
  fileDropEntry: NgxFileDropEntry | null;
  file: File | null;
};

@Component({
  selector: 'create-new-order-dialog',
  templateUrl: 'create-new-order-dialog.component.html',
  styleUrls: ['./create-new-order-dialog.component.css']
})
export class CreateNewOrderDialog {

  orderForm = new FormGroup({
    customerEmailAddress: new FormControl('', [Validators.required]),
    customerName: new FormControl('', [Validators.required]),
    deliveryDate: new FormControl('', [Validators.required]),
    reference: new FormControl('', [Validators.required]),
  });

  files: FileUploadItem[] = [];
  minDate = new Date();
  weekendFilter= (d: Date | null): boolean => {
    const day = (d || new Date()).getDay();
    return day !== 0 && day !== 6;
  };

  constructor(
    public dialogRef: MatDialogRef<CreateNewOrderDialog>,
    @Inject(MAT_DIALOG_DATA) public data: OrderData,
    private notificationService: NotificationService,
    private orderManagementService: OrderManagementService
  ) {
  }
  onClickCancel(): void {
    this.dialogRef.close();
  }

  onSaveClick() {
    const formData = new FormData();

    this.files.forEach((uploadedFile: FileUploadItem) => {
      if (uploadedFile.file != null && uploadedFile.fileDropEntry != null) {
        formData.append('files', uploadedFile.file, uploadedFile.fileDropEntry.fileEntry.name);
      }
    });

    formData.append('orderData', JSON.stringify(this.createOrderDto()));

    this.orderManagementService.saveNewOrder(formData).subscribe(result => this.dialogRef.close());
  }

  public dropped(files: NgxFileDropEntry[]) {
    for (const droppedFile of files) {
      if (!this.isFileAllowed(droppedFile.fileEntry.name)) {
        this.notificationService.showNotification('Fehler: der Typ der ausgewählten Datei stimmt nicht');
      }

      if (droppedFile.fileEntry.isFile && this.isFileAllowed(droppedFile.fileEntry.name)) {
        if (
          this.files.filter(
            (elem) => elem.fileDropEntry?.fileEntry.name === droppedFile.fileEntry.name
          ).length == 0
        ) {
          const uploadDocumentItem: FileUploadItem = {
            fileDropEntry: droppedFile,
            file: null,
          };

          this.files = this.files.concat(uploadDocumentItem);

          const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
          fileEntry.file((file) => {
            const foundFileObject = this.files.filter((value) => value.fileDropEntry?.fileEntry.name == file.name)[0];
            foundFileObject.file = file;
            if (this.isTooBig(file)) {
              this.notificationService.showNotification('Fehler: Datei ' + file.name + ' ist größer als die erlaubten 3 MB');
              this.removeFile(foundFileObject);
            }
          });
        }
      }
    }
  }

  private isTooBig(file: File) {
    return file.size > 3 * 1024 * 1024;
  }

  private isFileAllowed(fileName: string) {
    let isFileAllowed = false;
    let allowedFiles;

    allowedFiles = ['.jpg', '.jpeg', '.png'];
    const regex = /(?:\.([^.]+))?$/;
    const extension = regex.exec(fileName);
    if (extension) {
      for (const ext of allowedFiles) {
        if (ext === extension[0]) {
          isFileAllowed = true;
        }
      }
    }
    return isFileAllowed;
  }

  removeFile(fileUploadItem: FileUploadItem) {
    if (fileUploadItem.fileDropEntry != null) {
      const filename = fileUploadItem.fileDropEntry.fileEntry?.name;
      this.files = this.files.filter((elem) => {
        return elem.fileDropEntry?.fileEntry.name !== filename;
      });
    }
  }

  private createOrderDto() : OrderInputDto{
    const values = this.orderForm.getRawValue();
    return new OrderInputDto(
      values['customerEmailAddress']!!,
      values['customerName']!!,
      values['reference']!!,
      values['deliveryDate']!!
    );
  }
}
