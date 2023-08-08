import {Injectable, Injector, NgZone} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private injector: Injector, public snackBar: MatSnackBar, private readonly zone: NgZone) {}

  showNotification(notification: string): void {
    this.zone.run(() => {
      const snackBar = this.snackBar.open(notification, 'OK', {
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        duration: 3000,
      });
      snackBar.onAction().subscribe(() => {
        snackBar.dismiss();
      });
    });
  }
}
